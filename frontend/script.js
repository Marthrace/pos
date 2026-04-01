
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

            // ✅ Move this here, inside login
            localStorage.setItem("user", username);

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

        const table = document.getElementById("productsTable");

        // ✅ ADD THIS LINE (CRITICAL FIX)
        if (!table) return;

        table.innerHTML = "";

        products = data;

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

    })
    .catch(err => {
        console.error("Checkout load error:", err);
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

    openModal("Quantity", function(qty) {

        qty = parseInt(qty);
        if (!qty || qty <= 0) return;

        const product = products.find(p => p.id === id);
        if (!product) return;

        const existingItem = cart.find(i => i.productId === id);
        let totalRequested = qty;
        if (existingItem) totalRequested += existingItem.quantity;

        const msg = document.getElementById("modalMsg");

        // ❌ If stock exceeded → show message, do NOT close modal
        if (totalRequested > product.quantity) {
            msg.innerText = `❌ Stock less than ${totalRequested}`;
            msg.className = "modal-msg error";

            // Auto-clear after 3s, keep focus
            setTimeout(() => {
                msg.innerText = "";
                msg.className = "modal-msg";
                document.getElementById("modalInput").focus();
            }, 3000);

            return; // modal stays open
        }

        // ✅ Stock ok → add/update cart
        if (existingItem) {
            existingItem.quantity += qty;
        } else {
            cart.push({ productId: id, quantity: qty });
        }

        renderCart();

        // ✅ Close modal now that stock is fine
        document.getElementById("inputModal").style.display = "none";

    });

    // Clear modal input/message when opening
    const input = document.getElementById("modalInput");
    input.value = "";
    input.focus();

    const msg = document.getElementById("modalMsg");
    msg.innerText = "";
    msg.className = "modal-msg";
}

// new
let modalCallback = null;

function openModal(title, callback) {

    document.getElementById("modalTitle").innerText = title;

    const input = document.getElementById("modalInput");

    input.value = "";

    modalCallback = callback;

    document.getElementById("inputModal").style.display = "block";

    // ✅ ADD THIS
    setTimeout(() => {
        input.focus();
        input.select();
    }, 50);
}

document.addEventListener("DOMContentLoaded", function () {

    const input = document.getElementById("modalInput");

    if (input) {
        input.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                modalOk();
            }
        });
    }

});

function modalOk() {
    const val = document.getElementById("modalInput").value;

    if (modalCallback) {
        modalCallback(val); // let the caller decide what to do
    }
}

function modalCancel() {

    document.getElementById("inputModal").style.display = "none";

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

    openModal("Cash paid", function(paid) {
        

        if (!paid || isNaN(paid) || paid <= 0) return;

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
        .then(res => res.json())
        .then(order => {

            // ✅ FIX: use order.id instead of order.orderId
            window.location =
                `receipt.html?orderId=${order.id}`;

        })
        .catch(err => {
            console.error(err);
            alert("Checkout failed");
        });

    });
}
// Function to load sales report for admin users

function loadReport() {

    const auth = localStorage.getItem("auth");

    fetch("http://localhost:8080/reports/summary", {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => {

        const div = document.getElementById("report");

        // 🔴 HANDLE 403 (DISPLAY IN UI)
        if (res.status === 403) {
            div.innerHTML = `
                <h3 style="color:red;">Access Denied</h3>
                <p>You are not allowed to view reports.</p>
            `;
            return null;
        }

        // 🔴 HANDLE 401
        if (res.status === 401) {
            div.innerHTML = `
                <h3 style="color:red;">Session Expired</h3>
                <p>Please login again.</p>
            `;
            return null;
        }

        return res.json();
    })
    .then(data => {

        if (!data) return;

        const div = document.getElementById("report");

        div.innerHTML = "<h3>Summary</h3>";

        for (let key in data) {
            div.innerHTML += `<p>${key} : ${data[key]}</p>`;
        }

    })
    .catch(err => {
        console.error(err);

        document.getElementById("report").innerHTML = `
            <h3 style="color:red;">Error</h3>
            <p>Failed to load report.</p>
        `;
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
    
    openModal("Enter password to logout", function(password) {

        const authStored =
            localStorage.getItem("auth");

        const decoded =
            atob(authStored.split(" ")[1]);

        const username =
            decoded.split(":")[0];

        const auth =
            "Basic " + btoa(username + ":" + password);

        fetch("http://localhost:8080/products", {
            headers: {
                "Authorization": auth
            }
        })
        .then(res => {

            if (res.status === 200) {

                localStorage.clear();

                window.location = "login.html";

            } else {

                alert("Wrong password");

            }

        });

    });

}



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

function checkAdminUI() {

    const auth = localStorage.getItem("auth");

    if (!auth) return;

    const decoded =
        atob(auth.split(" ")[1]);

    const username =
        decoded.split(":")[0];

    if (username !== "admin") {

        const btn =
            document.getElementById("resetBtn");

        if (btn) btn.style.display = "none";

    }

}

function openResetModal() {

    document.getElementById("resetModal")
        .style.display = "block";

          // ✅ AUTO-FOCUS FIRST INPUT
    setTimeout(() => {
        document.getElementById("resetUsername").focus();
    }, 100);

}

function closeResetModal() {

    document.getElementById("resetModal").style.display = "none";

    // ✅ CLEAR INPUTS
    document.getElementById("resetUsername").value = "";
    document.getElementById("resetPassword").value = "";
    document.getElementById("confirmPassword").value = "";

    // ✅ CLEAR MESSAGE
    const msg = document.getElementById("resetMsg");
    msg.innerText = "";
    msg.className = "modal-msg";

    // ✅ RESET BUTTON TEXT (optional safety)
    const btn = document.getElementById("resetBtnAction");
    if (btn) btn.innerText = "Reset Password";

}


function resetPasswordAdmin() {

    const username =
        document.getElementById("resetUsername").value;

    const newPassword =
        document.getElementById("resetPassword").value;const confirmPassword =
    document.getElementById("confirmPassword").value;

            if (newPassword !== confirmPassword) {
                document.getElementById("resetMsg").innerText =
                    "❌ Passwords do not match";
                document.getElementById("resetMsg").className =
                    "modal-msg error";
                return;
            }
                if (!newPassword || !confirmPassword) {
    document.getElementById("resetMsg").innerText =
        "❌ Fill all fields";
    return;
}
    const auth =
        localStorage.getItem("auth");

    const msg =
        document.getElementById("resetMsg");

    const btn =
        document.getElementById("resetBtnAction");

    const decoded =
        atob(auth.split(" ")[1]);

    const adminUsername =
        decoded.split(":")[0];

    // 🔄 loading state
    msg.innerText = "Processing...";
    msg.className = "modal-msg";

    btn.disabled = true;
    btn.innerText = "Resetting...";

    fetch(
        "http://localhost:8080/users/reset-password",
        {
            method: "POST",
            headers: {
                "Content-Type":
                "application/x-www-form-urlencoded",
                "Authorization": auth
            },
            body:
                "adminUsername=" +
                encodeURIComponent(adminUsername) +
                "&username=" +
                encodeURIComponent(username) +
                "&newPassword=" +
                encodeURIComponent(newPassword)
        }
    )
    .then(res => {

        if (!res.ok) throw new Error();

        return res.text();

    })
    .then(() => {

        // ✅ SUCCESS (replaces alert)
        msg.innerText = "✅ Password reset successfully!";
        msg.className = "modal-msg success";

        btn.disabled = false;
        btn.innerText = "Done";

        setTimeout(() => {
            closeResetModal();
            msg.innerText = "";
            btn.innerText = "Reset Password";
        }, 2000);

    })
    .catch(() => {

        // ❌ ERROR (replaces alert)
        msg.innerText = "❌ Failed to reset password";
        msg.className = "modal-msg error";

        btn.disabled = false;
        btn.innerText = "Reset Password";

    });

}

function searchProducts() {
    const input = document.getElementById("productSearch").value.toLowerCase();
    const table = document.getElementById("productsTable");
    const rows = table.getElementsByTagName("tr");

    for (let i = 0; i < rows.length; i++) {
        const nameCell = rows[i].getElementsByTagName("td")[0]; // first column is name
        if (nameCell) {
            const name = nameCell.textContent.toLowerCase();
            if (name.indexOf(input) > -1) {
                rows[i].style.display = ""; // show row
            } else {
                rows[i].style.display = "none"; // hide row
            }
        }
    }
}


function loadCashierReport() {
    const auth = localStorage.getItem("auth");
    const username = localStorage.getItem("user"); // logged-in cashier

    fetch(`http://localhost:8080/reports/cashier/${username}`, {
        headers: {
            "Authorization": auth
        }
    })
    .then(res => {
        const div = document.getElementById("report");

        if (res.status === 403) {
            div.innerHTML = `
                <h3 style="color:red;">Access Denied</h3>
                <p>You cannot view this report.</p>
            `;
            return null;
        }

        if (res.status === 401) {
            div.innerHTML = `
                <h3 style="color:red;">Session Expired</h3>
                <p>Please login again.</p>
            `;
            return null;
        }

        return res.json();
    })
    .then(data => {
        if (!data) return;

        const div = document.getElementById("report");
        div.innerHTML = `<h3>Today's Sales for ${data.cashierUsername}</h3>`;

        data.items.forEach(item => {
            div.innerHTML += `
                <p>${item.productName} — Qty: ${item.quantitySold}, Total: ${item.total}</p>
            `;
        });

        div.innerHTML += `
            <p><strong>Total Items Sold: </strong>${data.totalItemsSold}</p>
            <p><strong>Total Cash: </strong>${data.totalCash}</p>
        `;
    })
    .catch(err => {
        console.error(err);
        document.getElementById("report").innerHTML = `
            <h3 style="color:red;">Error</h3>
            <p>Failed to load cashier report.</p>
        `;
    });
}

