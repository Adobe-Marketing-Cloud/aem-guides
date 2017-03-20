use(function () {
    'use strict';

    var i,
        coralIcons,
        cssClass = 'adobeExperienceManager';

    coralIcons = {
        'product:assets' : 'asset',
        'product:commerce': 'shoppingCart',
        'product:communities': 'users',
        'product:forms': 'form',
        'product:mobile': 'devicePhone',
        'product:platform': 'gears',
        'product:screens': 'railBottom',
        'product:sites': 'pages',
        'product:integrations': 'plug',
        'product:integrations/analytics': 'adobeAnalytics',
        'product:integrations/campaign': 'adobeMarketingCloud',
        'product:integrations/target': 'adobeTarget'
    };

    for (i = 0; i < this.tagIds.size(); i++) {
        if (coralIcons[this.tagIds.get(i)]) {
            cssClass = coralIcons[this.tagIds.get(i)];
            break;
        }
    }

    return {
        name: cssClass
    };
});
