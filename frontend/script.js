function login() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/products", {
        method: "GET",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        }
    })
    .then(res => {

        if (res.status === 200) {

            localStorage.setItem("auth",
                "Basic " + btoa(username + ":" + password));

            window.location = "products.html";

        } else {

            document.getElementById("msg").innerText =
                "Login failed";

        }

    });

}

// Function to handle product addition for admin users
function addProduct() {

    const name = document.getElementById("name").value;
    const price = document.getElementById("price").value;
    const quantity = document.getElementById("quantity").value;

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/products", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": auth
        },
        body: JSON.stringify({
            name: name,
            price: Number(price),
            quantity: Number(quantity)
        })
    })
    .then(res => {

        if (res.ok) {

    document.getElementById("msg").innerText =
        "Product added";

    loadProducts();

        } else {
            document.getElementById("msg").innerText =
                "Error adding product";
        }

    });

}

// Function to load products for checkout page
function loadProductsForCheckout() {

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/products", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => res.json())
    .then(data => {

        products = data;

        const table =
            document.getElementById("productsTable");

        table.innerHTML = "";

        data.forEach(p => {

            table.innerHTML += `
                <tr>
                    <td>${p.name}</td>
                    <td>${p.price}</td>
                    <td>${p.quantity}</td>
                    <td>
                        ${
                            p.quantity > 0
                            ? `<button onclick="addToCart(${p.id})">Add</button>`
                            : `Out of stock`
                        }
                    </td>
                </tr>
            `;

        });

    });

}

let cart = [];
let products = [];


function loadProducts() {

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/products", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => res.json())
    .then(data => {

        console.log(data); // VERY IMPORTANT

        const table =
            document.getElementById("productsTable");

        if (!table) return;

        table.innerHTML = "";

        data.forEach(p => {

            table.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.price}</td>
                    <td>${p.quantity}</td>
                </tr>
            `;

        });

    });

}
// Function to add products to cart with quantity input
function addToCart(id) {

    let qty =
        prompt("Quantity:");

    qty = parseInt(qty);

    if (!qty || qty <= 0) return;

    cart.push({
        productId: id,
        quantity: qty
    });

    renderCart();

}

// Function to render cart items and total amount
function renderCart() {

    const table =
        document.getElementById("cartTable");

    let total = 0;

    table.innerHTML = "";

    cart.forEach((i, index) => {

        const product =
            products.find(p => p.id === i.productId);

        total += product.price * i.quantity;

        table.innerHTML += `
            <tr>
                <td>${product.name}</td>
                <td>${i.quantity}</td>
                <td>
                    <button onclick="removeItem(${index})">
                        Remove
                    </button>
                </td>
            </tr>
        `;

    });

    document.getElementById("total").innerText =
        "Total: " + total;

}

function removeItem(index) {

    cart.splice(index, 1);

    renderCart();

}

// Function to handle checkout process
function checkout() {
    const auth = localStorage.getItem("auth");

    let paid = prompt("Enter cash paid by customer:");

    if (!paid || isNaN(paid) || paid <= 0) {
        return alert("Invalid amount");
    }

    paid = Number(paid);

    fetch("http://localhost:8080/checkout", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": auth
        },
        body: JSON.stringify({
            items: cart,
            paidAmount: paid,
            paymentMethod: "CASH"
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Checkout failed");
        return res.json();
    })
    .then(receipt => {
        window.location = `receipt.html?orderId=${receipt.orderId}`;
    })
    .catch(err => alert(err.message));
}
// Function to load sales report for admin users
function loadReport() {

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/reports/summary", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => res.json())
    .then(data => {

        const div = document.getElementById("report");

        div.innerHTML = "";

        for (let key in data) {

            div.innerHTML +=
                "<p>" + key + " : " + data[key] + "</p>";

        }

    });

}

// Navigation functions
function goProducts() {
    window.location = "products.html";
}

function goCheckout() {
    window.location = "checkout.html";
}

function goReports() {
    window.location = "reports.html";
}

// Logout function to clear authentication and redirect to login page
function logout() {

    const authStored =
        localStorage.getItem("auth");

    if (!authStored) {
        window.location = "login.html";
        return;
    }

    const decoded =
        atob(authStored.split(" ")[1]);

    const username =
        decoded.split(":")[0];

    const password =
        prompt("Enter password to logout:");

    if (!password) return;

    const auth =
        "Basic " + btoa(username + ":" + password);

    fetch("http://localhost:8080/products", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => {

        if (res.status === 200) {

            localStorage.removeItem("auth");
            localStorage.removeItem("user");

            window.location = "login.html";

        } else {

            alert("Wrong password");

        }

    });

}
// Set logged in user in localStorage for role-based access control
localStorage.setItem("user", username);
// Hide add product button for non-admin users
window.onload = function () {

    const user = localStorage.getItem("user");

    if (user !== "admin") {

        const btn = document.getElementById("addBtn");

        if (btn) btn.style.display = "none";

    }

};

function clearCart() {

    cart = [];

    renderCart();

}

// Signup function for creating new users
function signup() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("role").value;

    fetch("http://localhost:8080/users/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            "username=" + encodeURIComponent(username) +
            "&password=" + encodeURIComponent(password) +
            "&role=" + encodeURIComponent(role)
    })
    .then(res => {

        if (!res.ok) {
            throw new Error("Request failed");
        }

        return res.text();
    })
    .then(data => {
        document.getElementById("msg").innerText =
            "User created successfully";
            window.location.href = "login.html"; 
    })


    
    .catch(err => {
        console.error(err);
        document.getElementById("msg").innerText =
            "Error creating user";
    });
}

//checkauthentication function to protect pages
function checkAuth() {

    const auth =
        localStorage.getItem("auth");

    if (!auth) {

        window.location = "login.html";

    }

}


function loadLowStock() {

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/products/low", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => res.json())
    .then(data => {

        let div =
            document.getElementById("report");

        div.innerHTML +=
            "<h3>Low Stock</h3>";

        data.forEach(p => {

            div.innerHTML +=
                p.name + " qty=" + p.quantity + "<br>";

        });

    });

}
