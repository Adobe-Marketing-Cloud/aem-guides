/*globals com */
use(["/libs/wcm/foundation/components/utils/Image.js"],function (Image) {
    var assetId,
        trackable = false,
        param = this.path,
        assetResource,
        image,
        path,
        asset;

    param = param ? param.trim() : null;

    if (!param || param.length === 0) {
        image = new Image(resource);
        path = image.fileReference();
    } else if (param.indexOf('/') !== 0) {
      // Param looks like a relative path
      image = new Image(resource.getChild(param));
      path = image.fileReference;
    } else {
      // Param looks like an absolute path
      path = param;
    }

    assetResource = resource.resourceResolver.getResource(path);
    if (assetResource) {
      asset = com.day.cq.dam.commons.util.DamUtil.resolveToAsset(assetResource);
    
      if (asset) {
          assetId = asset.getID();
          trackable = assetId && assetId.length() > 0;
      }
    }

    return {
        trackable: trackable,
        id: assetId
    };    
});