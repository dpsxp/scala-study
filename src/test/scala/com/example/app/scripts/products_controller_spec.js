describe('ProductsController', function() {
  beforeEach(module('app'));

  var $controller, controller, scope;

  beforeEach(inject(function(_$controller_){
    $controller = _$controller_;
    $scope = {};
    controller = $controller('ProductsController', { $scope: $scope });
  }));

  describe('$scope.resetProduct', function() {
    it('resets the product', function() {
      $scope.product.name = "fake";
      $scope.product.price = 90;
      $scope.resetProduct();

      expect($scope.product.name).toEqual("");
      expect($scope.product.price).toEqual("");
    });
  });

  describe("$scope.edit", function() {
    it('sets the given product as the current product', function() {
      var product = { name: 'fake', price: 100 };
      $scope.edit(product);

      expect($scope.product).toEqual(product);
    });
  });

  describe("$scope.submit", function() {
    it('calls the update on resource when the product is not a new product', function() {
      spyOn($scope, 'save');
      $scope.product = { name: 'fake', price: 100 };
      $scope.submit();
      expect($scope.save).toHaveBeenCalledWith($scope.product);
    });

    it('calls the save on resource when the product is a new product', function() {
      spyOn($scope, 'update');
      $scope.product = { name: 'fake', price: 100, id: '1204i124012' };
      $scope.submit();
      expect($scope.update).toHaveBeenCalledWith($scope.product);
    });
  });

  describe("$scope.update", function() {
    var $httpBackend, product = { id: 90, name: "tester", price: 90 };

    beforeEach(inject(function($injector) {
      $httpBackend = $injector.get('$httpBackend');
      $httpBackend.when('PUT', '/products/' + product.id).respond({});
      $httpBackend.when('GET', '/products').respond([]);
    }));


    it("calls the update method on resource", inject(function(ProductsResource, $httpParamSerializer) {
      spyOn(ProductsResource, 'update').and.callThrough();
      $scope.update(product);
      expect(ProductsResource.update).toHaveBeenCalledWith({ id: product.id }, $httpParamSerializer(product));
    }));

    it("resets the current product when the update was ok", inject(function(ProductsResource) {
      spyOn($scope, 'resetProduct');
      $scope.update(product);
      $httpBackend.flush();
      expect($scope.resetProduct).toHaveBeenCalled();
    }));
  });

  describe("$scope.save", function() {
    var $httpBackend, product = { name: "tester", price: 90 };

    beforeEach(inject(function($injector) {
      $httpBackend = $injector.get('$httpBackend');
      $httpBackend.when('POST', '/products').respond(product);
      $httpBackend.when('GET', '/products').respond([]);
    }));

    it("calls the save method on resource", inject(function(ProductsResource, $httpParamSerializer) {
      spyOn(ProductsResource, 'save').and.callThrough();
      $scope.save(product);
      expect(ProductsResource.save).toHaveBeenCalledWith($httpParamSerializer(product));
    }));

    it("adds the new product on $scope.products when the the save was ok", inject(function(ProductsResource) {
      spyOn(ProductsResource, 'save').and.callThrough();
      $scope.save(product);
      $httpBackend.flush();
      expect($scope.products.length).toEqual(1);
    }));

    it("resets the current product object when the save was ok", inject(function(ProductsResource) {
      spyOn(ProductsResource, 'save').and.callThrough();
      $scope.save(product);
      $httpBackend.flush();
      expect($scope.product).toEqual({ name: "", price: "" });
    }));
  });

  describe("$scope.remove", function() {
    var $httpBackend, id = 3;

    beforeEach(inject(function($injector) {
      $httpBackend = $injector.get('$httpBackend');
      $httpBackend.when('DELETE', '/products/' + id).respond({});
      $httpBackend.when('GET', '/products').respond([]);
    }));

    it("calls the delete method on resource", inject(function(ProductsResource) {
      spyOn(ProductsResource, 'delete').and.callThrough();
      $scope.remove(3);
      expect(ProductsResource.delete).toHaveBeenCalledWith({ id: 3 });
    }));

    it("removes the product from the $scope.products when the delete was ok", inject(function(ProductsResource) {
      var product = { id: id, name: 'hello', price: 90 };
      $scope.products.push(product);
      $scope.remove(id);
      $httpBackend.flush();
      expect($scope.products.indexOf(product)).toEqual(-1);
    }));
  });
});
