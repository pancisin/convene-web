const InjectorGenerator = {};

function getFnParamNames (fn) {
  var fstr = fn.toString();
  return fstr.match(/\(.*?\)/)[0].replace(/[()]/gi, '').replace(/\s/gi, '').split(',');
}

InjectorGenerator.generate = (apiReference, resourceId) => {
  return new Proxy(apiReference, {
    get (target, propKey, receiver) {
      const property = target[propKey];

      if (typeof property === 'function') {
        return (...args) => {
          if (getFnParamNames(property).includes('id')) {
            args.unshift(resourceId);
          }

          return property.apply(this, args);
        };
      } else {
        return property;
      }
    }
  });
};

export default InjectorGenerator;
