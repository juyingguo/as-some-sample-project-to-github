#include "mp3_encoder.h"
#include "../../common/CommonTools.h"

#define LOG_TAG "Mp3Encoder"

Mp3Encoder::Mp3Encoder() {
}

Mp3Encoder::~Mp3Encoder() {
}

int Mp3Encoder::Init(const char* pcmFilePath, const char *mp3FilePath, int sampleRate, int channels, int bitRate) {
	int ret = -1;
	pcmFile = fopen(pcmFilePath, "rb");
	if(pcmFile) {
	    LOGI("Mp3Encoder::Init,fopen ok for pcmFilePath:%s.", pcmFilePath);
		mp3File = fopen(mp3FilePath, "wb");
		if(mp3File) {
		    LOGI("Mp3Encoder::Init,fopen ok for mp3File:%s.", mp3File);
			lameClient = lame_init();
			lame_set_in_samplerate(lameClient, sampleRate);
			lame_set_out_samplerate(lameClient, sampleRate);
			lame_set_num_channels(lameClient, channels);
			lame_set_brate(lameClient, bitRate / 1000);
			lame_init_params(lameClient);
			ret = 0;
		}else{
            LOGI("Mp3Encoder::Init,fopen fail for mp3File:%s.", mp3File);
        }
	}else{
        LOGI("Mp3Encoder::Init,fopen fail for pcmFilePath:%s.", pcmFilePath);
	}
	return ret;
}
/*
 * 其次是Encode方法的实现，函数主体是一个循环，每次都会读取一段bufferSize大小的PCM数据buffer，然后再编码该buffer，
 * 但是在编码buffer之前得把该buffer的左右声道拆分开，再送入到LAME编码器，最后将编码之后的数据写入MP3文件中。
 */
void Mp3Encoder::Encode() {
    LOGI("Mp3Encoder::Encode() enter.");
	int bufferSize = 1024 * 256;
	short* buffer = new short[bufferSize / 2];
	short* leftBuffer = new short[bufferSize / 4];
	short* rightBuffer = new short[bufferSize / 4];
	uint8_t* mp3_buffer = new uint8_t[bufferSize];
	int readBufferSize = 0;
	while ((readBufferSize = fread(buffer, 2, bufferSize / 2, pcmFile)) > 0) {
		for (int i = 0; i < readBufferSize; i++) {
			if (i % 2 == 0) {
				leftBuffer[i / 2] = buffer[i];
			} else {
				rightBuffer[i / 2] = buffer[i];
			}
		}
		int wroteSize = lame_encode_buffer(lameClient, (short int *) leftBuffer, (short int *) rightBuffer, readBufferSize / 2, mp3_buffer, bufferSize);
        LOGI("Mp3Encoder::Encode() readBufferSize=%d,wroteSize=%d\n",readBufferSize,wroteSize);
		fwrite(mp3_buffer, 1, wroteSize, mp3File);
	}
	delete[] buffer;
	delete[] leftBuffer;
	delete[] rightBuffer;
	delete[] mp3_buffer;
}

void Mp3Encoder::Destory() {
	if(pcmFile) {
		fclose(pcmFile);
	}
	if(mp3File) {
		fclose(mp3File);
		lame_close(lameClient);
	}
}
