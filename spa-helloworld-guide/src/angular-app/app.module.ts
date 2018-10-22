import './components/mapping';
import { BrowserModule, BrowserTransferStateModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { APP_BASE_HREF } from '@angular/common';
import { AngularWeatherWidgetModule, WeatherApiName } from 'angular-weather-widget';
import { AppComponent } from './app.component';
import { SpaAngularEditableComponentsModule } from '@adobe/cq-angular-editable-components';
import { ModelManagerService } from './components/model-manager.service';
import { TextComponent } from './components/text/text.component';
import { ImageComponent } from './components/image/image.component';
import { WeatherComponent } from './components/weather/weather.component';
import { NavigationComponent } from './components/navigation/navigation.component';
import { MenuComponent } from './components/navigation/menu/menu.component';
import { MainContentComponent } from './components/main-content/main-content.component';
import { AppRoutingModule } from './app-routing.module';
import { HelloWorldComponent } from './components/helloworld/helloworld.component';

@NgModule({
  imports: [BrowserModule.withServerTransition({ appId: 'we-retail-sample-angular' }),
    SpaAngularEditableComponentsModule,
  AngularWeatherWidgetModule.forRoot({
    key: "37375c33ca925949d7ba331e52da661a",
    name: WeatherApiName.OPEN_WEATHER_MAP,
    baseUrl: 'http://api.openweathermap.org/data/2.5'
  }),
    AppRoutingModule,
    BrowserTransferStateModule],
  providers: [ModelManagerService,
    { provide: APP_BASE_HREF, useValue: '/' }],
  declarations: [AppComponent,
    TextComponent,
    ImageComponent,
    WeatherComponent,
    NavigationComponent,
    HelloWorldComponent,
    MenuComponent,
    MainContentComponent],
  entryComponents: [TextComponent,
    ImageComponent, WeatherComponent, NavigationComponent, MainContentComponent, HelloWorldComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }

