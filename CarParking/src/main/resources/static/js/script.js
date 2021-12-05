const paymentStart = () => {
  var amount = $("#payment_field").val();
  if (amount == "" || amount == null) {
    swal("Failed ", "amount is required ", "error");
    return;
  }

  $.ajax({
    url: "/admin/create_order",
    data: JSON.stringify({ amount: amount, info: "order_request" }),
    contentType: "application/json",
    type: "POST",
    dataType: "json",
    success: function (response) {
      if (response.status == "created") {
        let options = {
          key: "rzp_test_3fGEPJTbBw4c9f",
          amount: response.amount,
          currency: "INR",
          name: "Car Parking",
          description: "Payment",
          order_id: response.id,
          handler: function (response) {
            swal("Payment successful ");
          },
          prefill: {
            name: "",
            email: "",
            contact: "",
          },

          notes: {
            address: "OOP project ",
          },
          theme: {
            color: "#DA0037",
          },
        };

        let rzp = new Razorpay(options);
        rzp.on("payment.failed", function (response) {
          swal("Failed ", "Oops payment failed ", "error");
        });
        rzp.open();
      }
    },
    error: function (error) {
      alert("Something went wrong ");
    },
  });
};

const paymentAdd = () => {
  var amount = $("#payment_field").val();
  if (amount == "" || amount == null) {
    swal("Failed ", "Amount is required ", "error");
    return;
  }
  $.ajax({
    url: "/admin/add_money",
    data: JSON.stringify({ amount: amount, info: "order_request" }),
    contentType: "application/json",
    type: "POST",
    dataType: "json",
    success: function (response) {
      console.log(response);
      if (response.status == "created") {
        let options = {
          key: "rzp_test_3fGEPJTbBw4c9f",
          amount: response.amount,
          currency: "INR",
          name: "Car Parking",
          description: "Payment",
          order_id: response.id,
          handler: function (response) {
            swal("Congrats Payment successful ");
          },
          prefill: {
            name: "",
            email: "",
            contact: "",
          },

          notes: {
            address: "OOP project ",
          },
          theme: {
            color: "#DA0037",
          },
        };

        let rzp = new Razorpay(options);

        rzp.on("payment.failed", function (response) {
          swal("Failed", "Oops payment failed ", "error");
        });
        rzp.open();
      }
    },
    error: function (error) {
      alert("Something went wrong ");
    },
  });
};
