const InjectorGenerator = {};

InjectorGenerator.generate = (apiReference, resourceId) => {
  return new Proxy(apiReference, {
    get (target, propKey, receiver) {
      const property = target[propKey];

      if (typeof property === 'function') {
        return (...args) => {
          if (apiReference.hasOwnProperty(property.name)) {
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
