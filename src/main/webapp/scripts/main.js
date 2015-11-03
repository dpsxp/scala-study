var mod = angular.module('app', ["ngResource"]);

mod.filter("toMoney", function() {
  return function(input) {
    return "R$" + input.toFixed(2);
  };
});

mod.factory("ProductsResource", ["$resource", function($resource) {
  return $resource("/products/:id", { id: '@id' }, {
    update: {
      method: 'PUT'
    }
  });
}]);

mod.controller('Products',
  ["$scope", "$httpParamSerializer", "ProductsResource",
  function($scope, $httpParamSerializer, ProductsResource) {
    var createProduct = function() {
      return {
        name: "",
        price: ""
      };
    };

    $scope.resetProduct = function() {
      $scope.product = createProduct();
    };

    $scope.resetProduct();
    $scope.products = ProductsResource.query();

    $scope.edit = function(product) {
      $scope.product = product;
    };

    $scope.submit = function() {
      event.preventDefault();
      var product = $scope.product;

      if ("id" in product) {
        $scope.update(product);
      } else {
        $scope.save(product);
      }
    };

    $scope.update = function(product) {
      ProductsResource.update({ id: product.id }, $httpParamSerializer(product))
        .$promise
        .then(function() {
          $scope.resetProduct();
        });
    };

    $scope.save = function(product) {
      ProductsResource.save($httpParamSerializer(product))
        .$promise
        .then(function(response) {
          $scope.products.unshift(response.product);
        })
      .then(function() {
        $scope.resetProduct();
      });
    };

    $scope.remove = function(id) {
      ProductsResource.delete({ id: id })
        .$promise
        .then(function() {
          $scope.products = $scope.products.filter(function(prod) {
            return prod.id !== id;
          });
        });
    };
  }
]);

mod.run(["$http", function($http) {
  $http.defaults.headers.post["Content-Type"] = 'application/x-www-form-urlencoded';
  $http.defaults.headers.put["Content-Type"] = 'application/x-www-form-urlencoded';
}]);
