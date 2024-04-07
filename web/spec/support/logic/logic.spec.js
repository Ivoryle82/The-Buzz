// Define the add function
function add(a, b) {
    return a + b;
  }
// logic.spec.js
describe("Logic Component", function() {
    it("should add two numbers correctly", function() {
      expect(add(1, 2)).toEqual(3);
    });
  
    it("should handle edge cases", function() {
      expect(add(0, 0)).toEqual(0);
      expect(add(-1, 1)).toEqual(0);
    });
  });
  