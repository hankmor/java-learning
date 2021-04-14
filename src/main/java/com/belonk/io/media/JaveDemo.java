package com.belonk.io.media;

/**
 * Created by sun on 2020/9/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class JaveDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// String fileName   = "path/to/my/file";
		// File file         = new File(fileName);
		// MediaInfo info    = new MediaInfo();
		// info.open(file);
		//
		// String format     = info.get(MediaInfo.StreamKind.Video, i, "Format",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
		// int bitRate       = info.get(MediaInfo.StreamKind.Video, i, "BitRate",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
		// float frameRate   = info.get(MediaInfo.StreamKind.Video, i, "FrameRate",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
		// short width       = info.get(MediaInfo.StreamKind.Video, i, "Width",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
		//
		// int audioBitrate  = info.get(MediaInfo.StreamKind.Audio, i, "BitRate",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
		// int audioChannels = info.get(MediaInfo.StreamKind.Audio, i, "Channels",
		// 		MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
	}

	public static void convertAnyAudioToMp3() {
		boolean succeeded;
		try {
			// File source = new File("file path");
			// File target = new File("file path");
			//
			// //Audio Attributes
			// AudioAttributes audio = new AudioAttributes();
			// audio.setCodec("libmp3lame");
			// audio.setBitRate(128000);
			// audio.setChannels(2);
			// audio.setSamplingRate(44100);
			//
			// //Encoding attributes
			// EncodingAttributes attrs = new EncodingAttributes();
			// attrs.setFormat("mp3");
			// attrs.setAudioAttributes(audio);
			//
			// //Encode
			// Encoder encoder = new Encoder();
			// encoder.encode(new MultimediaObject(source), target, attrs);

		} catch (Exception ex) {
			ex.printStackTrace();
			succeeded = false;
		}
	}
}