
export default class InjectorGenerator {

  static generate (apiReference, resourceId) {
    return new Proxy({ ...apiReference }, {
      get (target, propKey, receiver) {
        const property = target[propKey];

        if (typeof property === 'function') {
          return function (...args) {
            if (target.hasOwnProperty(property.name)) {
              return property.apply(null, [resourceId, ...args]);
            }

            return property.apply(null, args);
          };
        } else {
          return property;
        }
      }
    });
  };
};
