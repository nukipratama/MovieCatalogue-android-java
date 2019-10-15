package com.example.gdk19_nukipratama.RemoteViews;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MovieStackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
