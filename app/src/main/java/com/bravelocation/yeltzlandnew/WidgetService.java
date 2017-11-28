package com.bravelocation.yeltzlandnew;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by john on 28/11/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
