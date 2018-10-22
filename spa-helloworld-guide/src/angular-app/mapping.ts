
import { TextComponent } from "./text/text.component";
import { ImageComponent } from "./image/image.component";
import { WeatherComponent } from "./weather/weather.component";
import { NavigationComponent } from "./navigation/navigation.component";
import { HelloWorldComponent } from "./helloworld/helloworld.component";
import { MapTo, AEMContainerComponent, AEMResponsiveGridComponent } from "@adobe/cq-angular-editable-components";

/**
 * Default Edit configuration for the Image component that interact with the Core Image component and sub-types
 *
 * @type EditConfig
 */
const ImageEditConfig = {

    emptyLabel: 'Image',

    isEmpty: function(cqModel) {
        return !cqModel || !cqModel.src || cqModel.src.trim().length < 1;
    }
};

/**
 * Default Edit configuration for the Text component that interact with the Core Text component and sub-types
 *
 * @type EditConfig
 */
const TextEditConfig = {

    emptyLabel: 'Text',

    isEmpty: function(cqModel) {
        return !cqModel || !cqModel.text || cqModel.text.trim().length < 1;
    }
};

const HelloWorldEditConfig = {

    emptyLabel: 'Hello World',

    isEmpty: function(props) {
        return !props || !props.displayMessage || props.displayMessage.trim().length < 1;
    }
};

MapTo('we-retail-journal/components/text')(TextComponent, TextEditConfig);
MapTo('we-retail-journal/components/image')(ImageComponent, ImageEditConfig);
MapTo('wcm/foundation/components/responsivegrid')(AEMResponsiveGridComponent);
MapTo('we-retail-journal/components/weather')(WeatherComponent);
MapTo('we-retail-journal/components/navigation')(NavigationComponent);
MapTo('we-retail-journal/angular/components/structure/app')(AEMContainerComponent);
MapTo('we-retail-journal/components/helloworld')(HelloWorldComponent, HelloWorldEditConfig);

