/*
 The MIT License (MIT)

 Copyright (c) 2011 Asier <info@infinityk.com>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package com.infinityk.simplesoundengine;

import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

/**
 *
 * @author Asier
 */
public class SimpleSoundEngine {
    public static final int MAX_SOURCES = 32;
    
    private final HashMap<String,SoundBufferEntry> buffers;
    private final LinkedList<SoundSourceEntry> sources;
    private final Streamer musicStreamer;
    private boolean initiated;
    
    private final FloatBuffer sourcePos;
    private final FloatBuffer sourceVel;
    private static final Logger logger = Logger.getLogger(SimpleSoundEngine.class.getName());
    private final VirtualFileSystem vfs;

    public SimpleSoundEngine(VirtualFileSystem vfs) {
        buffers = new HashMap<>();
        sources = new LinkedList<>();
        musicStreamer = new Streamer(vfs);
        sourcePos = BufferUtils.createFloatBuffer(3);
        sourceVel = BufferUtils.createFloatBuffer(3);
        this.vfs = vfs;
    }
    
    //<editor-fold defaultstate="collapsed" desc="SoundEngine">
    public void init(){
        if(!initiated){
            try {
                AL.create();
                //init the listener
                FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(
                        new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});
                FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(
                        new float[]{0.0f, 0.0f, 0.0f});
                FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(
                        new float[]{0.0f, 0.0f, 0.0f});
                listenerPos.flip();
                listenerVel.flip();
                listenerOri.flip();
                AL10.alListener(AL10.AL_POSITION, listenerPos);
                AL10.alListener(AL10.AL_VELOCITY, listenerVel);
                AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
                //init streamer
                musicStreamer.init();
                //pregenerate sound sources
                int validSources = 0;
                for(int i = 0; i < MAX_SOURCES; i++){
                    SoundSourceEntry e = new SoundSourceEntry();
                    AL10.alGenSources(e.sourceId);
                    if (AL10.alGetError() == AL10.AL_NO_ERROR) {
                        sources.add(e);
                        validSources++;
                    }
                }
                logger.log(Level.INFO, "SoundEngine initiated with {0} sources", validSources);
                initiated = true;
            } catch (LWJGLException ex) {
                logger.log(Level.WARNING, "Initiating sound engine", ex);
            }
        }
    }
    
    public void destroy(){
        if (initiated){
            unloadAllSounds();
            for(SoundSourceEntry e : sources){
                AL10.alDeleteSources(e.sourceId);
            }
            musicStreamer.destroy();
            AL.destroy();
            sources.clear();
            initiated = false;
        }
    }
    
    public void setMute(boolean mute){
        if (initiated){
            if(mute){
                AL10.alListenerf(AL10.AL_GAIN, 0.0f);
            }else{
                AL10.alListenerf(AL10.AL_GAIN, 1.0f);
            }
        }
    }

    public void poll(){
        musicStreamer.poll();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Sounds">
    public void unloadAllSounds(){
        stopAllSounds();
        for(String s : buffers.keySet()){
            SoundBufferEntry e = buffers.get(s);
            AL10.alDeleteBuffers(e.bufferId);
        }
        buffers.clear();
    }
    
    public void unloadUselessSounds(){
        for(Iterator<Entry<String, SoundBufferEntry>> it = buffers.entrySet().iterator(); it.hasNext(); ){
            Entry<String,SoundBufferEntry> e = it.next();
            if(e.getValue().playingAt.isEmpty()) {
                AL10.alDeleteBuffers(e.getValue().bufferId);
                it.remove();
            }
        }
    }
    
    public void loadSound(String path){
        if (!buffers.containsKey(path)){
            SoundData data = SoundData.create(path,vfs);
            SoundBufferEntry e = new SoundBufferEntry();
            AL10.alGenBuffers(e.bufferId);
            AL10.alBufferData(e.bufferId.get(0), 
                    data.format, data.data, data.samplerate);
            buffers.put(path, e);
        }
    }
    
    public void unloadSound(String path){
        SoundBufferEntry buffer = buffers.get(path);
        if(buffer == null){ return; }
        for(SoundSourceEntry source : buffer.playingAt.keySet()){
            AL10.alSourceStop(source.sourceId);
            AL10.alSourcei(source.sourceId.get(0), AL10.AL_BUFFER, 0);
            source.setLastPlayed(null);
        }
        buffer.playingAt.clear();
        buffers.remove(path);
    }

    public void playSound(String path, float gain, float pitch){
        SoundSourceEntry entry = findFreeSource();
        if (entry == null){
            logger.log(Level.WARNING,"No free sources available");
            return;
        }
        int sourceId = entry.sourceId.get(0);
        loadSound(path);
        SoundBufferEntry buffer = buffers.get(path);
        buffer.playingAt.put(entry, entry);
        entry.setLastPlayed(buffer);
        int bufferId = buffer.bufferId.get(0);
        AL10.alSourceStop(sourceId);
	AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
	AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	AL10.alSourcef(sourceId, AL10.AL_GAIN, gain); 
	AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_FALSE);
	sourcePos.clear();
	sourceVel.clear();
	sourceVel.put(new float[] { 0, 0, 0 });
	sourcePos.put(new float[] { 0, 0, 0 });
	sourcePos.flip();
	sourceVel.flip();
	AL10.alSource(sourceId, AL10.AL_POSITION, sourcePos);
    	AL10.alSource(sourceId, AL10.AL_VELOCITY, sourceVel);
	AL10.alSourcePlay(sourceId); 			
    }
    
    
    public void stopAllSounds(){
        for(SoundSourceEntry e : sources){
            AL10.alSourceStop(e.sourceId);
            AL10.alSourcei(e.sourceId.get(0), AL10.AL_BUFFER, 0);
            e.setLastPlayed(null);
        }
        for(String s : buffers.keySet()){
            buffers.get(s).playingAt.clear();
        }
    }
    
    private SoundSourceEntry findFreeSource(){
        Iterator<SoundSourceEntry> i = sources.iterator();
        while(i.hasNext()){
            SoundSourceEntry source = i.next();
            int status = AL10.alGetSourcei(source.sourceId.get(0), AL10.AL_SOURCE_STATE);
            if ((status != AL10.AL_PLAYING) && (status != AL10.AL_PAUSED)) {
                SoundBufferEntry buffer = source.getLastPlayed();
                if(buffer != null){
                    buffer.playingAt.remove(source);
                    source.setLastPlayed(null);
                }
                i.remove(); 
                sources.add(source);
                return source;
            }	
        }
        return null;
    }
    public void playMusic(String path, boolean loop) throws IOException{
        musicStreamer.setSourceFile(path);
        musicStreamer.play(loop);
    }
    
    public void stopMusic(){
        musicStreamer.stop();
    }
    
    public void pauseMusic(boolean pause){
        musicStreamer.setPause(pause);
    }
    
    public void setMusicGain(float g) {
        this.musicStreamer.setGain(g);
    }
}
