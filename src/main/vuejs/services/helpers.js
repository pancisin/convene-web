export function calculateHash (input) {
  var hash = 0;

  if (input == null || input.length === 0) return hash;
  for (var i = 0; i < input.length; i++) {
    var chr = input.charCodeAt(i);
    hash = ((hash << 5) - hash) + chr;
    hash |= 0;
  }

  return hash;
}

export function compareHash (a, b) {
  return calculateHash(a) === calculateHash(b);
}

export function validUrl (str) {
  var pattern = new RegExp('((http|https)(:\/\/))?([a-zA-Z0-9]+[.]{1}){2}[a-zA-z0-9]+(\/{1}[a-zA-Z0-9]+)*\/?', 'i');
  if (!pattern.test(str)) {
    return false;
  } else {
    return true;
  }
}

export function groupBy (array, f) {
  var groups = {};
  array.forEach(o => {
    var group = JSON.stringify(f(o));
    groups[group] = groups[group] || [];
    groups[group].push(o);
  });
  return Object.keys(groups).map(function (group) {
    const flag_element = groups[group][0];
    return {
      data: groups[group],
      props: f(flag_element)
    };
  });
}
