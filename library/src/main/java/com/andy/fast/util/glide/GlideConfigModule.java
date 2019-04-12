package com.andy.fast.util.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;


/**
 * create by 郭恩博 on 2019/4/12
 */
@GlideModule
public class GlideConfigModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //修改默认配置，如缓存配置
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context));
        builder.build(context);
    }


}
