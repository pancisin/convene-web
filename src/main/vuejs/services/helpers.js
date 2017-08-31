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
