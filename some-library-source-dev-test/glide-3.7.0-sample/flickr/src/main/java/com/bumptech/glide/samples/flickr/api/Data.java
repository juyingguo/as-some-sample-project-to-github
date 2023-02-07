package com.bumptech.glide.samples.flickr.api;

final class Data {
  static final String BASE = "http://i.imgur.com/";
  static final String EXT = ".jpg";

  static final String[] URLS_CUSTOM = new String[]{//image use from *\as_project\androidFrame\Android-Universal-Image-Loader
          // Heavy images
          "https://img-pre.ivsky.com/img/tupian/pre/202012/25/jiebing_zhiwu-001.jpg",
          "https://img-pre.ivsky.com/img/tupian/pre/202012/25/jiebing_zhiwu-002.jpg",
          "https://img-pre.ivsky.com/img/tupian/pre/202012/25/jiebing_zhiwu-003.jpg",
          "https://img-pre.ivsky.com/img/tupian/pre/202012/25/jiebing_zhiwu-004.jpg",
			// Light images
			"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg10.360buyimg.com%2Fn1%2Fjfs%2Ft1%2F24680%2F11%2F6235%2F228967%2F5c4aa4a8Ef6767301%2Fea8dac2961eb24c0.jpg&refer=http%3A%2F%2Fimg10.360buyimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638797781&t=1ed22386802bae768fff791313f34a15",
			"https://img0.baidu.com/it/u=1964164365,3191594877&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800",
			"https://img1.baidu.com/it/u=2097245761,2344583317&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500",
//			// Special cases
//			"http://cdn.urbanislandz.com/wp-content/uploads/2011/10/MMSposter-large.jpg", // Very large image
//			"https://alifei04.cfp.cn/creative/vcg/veer/612/veer-168419413.jpg?x-oss-process=image/format,webp", // WebP image
//			"http://4.bp.blogspot.com/-LEvwF87bbyU/Uicaskm-g6I/AAAAAAAAZ2c/V-WZZAvFg5I/s800/Pesto+Guacamole+500w+0268.jpg", // Image with "Mark has been invalidated" problem
//			"file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
//			"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
//			"drawable://" + R.drawable.ic_launcher, // Image from drawables
//			"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link with UTF-8
//			"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
//			"http://bit.ly/soBiXr", // Redirect link
//			"http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
//			"", // Empty link
//			"http://wrong.site.com/corruptedLink", // Wrong link
  };
  static final String[] URLS = URLS_CUSTOM; /*{
      BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
      BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
      BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
      BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
      BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
      BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
      BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
      BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
      BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
      BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
      BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
      BASE + "z77CaIt" + EXT,
  };*/
  private Data() {
    // No instances.
  }
}
