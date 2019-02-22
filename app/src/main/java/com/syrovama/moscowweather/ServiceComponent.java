package com.syrovama.moscowweather;

import dagger.Component;

@ServiceScope
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
        WebAPI getWebAPI();
}
