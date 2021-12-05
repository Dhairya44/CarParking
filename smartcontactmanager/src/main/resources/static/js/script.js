console.log("this is script file");

const toggleSidebar = () => {
  if ($(".sidebar").is(":visible")) {
    //true
    //band karna hai
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
  } else {
    //false
    //show karna hai
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "20%");
  }
};

const search = () => {
  // console.log("searching...");

  let query = $("#search-input").val();

  if (query == "") {
    $(".search-result").hide();
  } else {
    //search
    console.log(query);

    //sending request to server

    let url = `http://localhost:8080/search/${query}`;

    fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        //data......
        // console.log(data);

        let text = `<div class='list-group'>`;

        data.forEach((worker) => {
          text += `<a href='/user/${worker.cId}/worker' class='list-group-item list-group-item-action'> ${worker.name}  </a>`;
        });

        text += `</div>`;

        $(".search-result").html(text);
        $(".search-result").show();
      });
  }
};

//first request- to server to create order

const paymentStart = () => {
  console.log("payment started..");
  var amount = $("#payment_field").val();
  console.log(amount);
  if (amount == "" || amount == null) {
    // alert("amount is required !!");
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
      //invoked when success
      console.log(response);
      if (response.status == "created") {
        //open payment form
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
      //invoked when success
      console.log(response);
      if (response.status == "created") {
        //open payment form
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
