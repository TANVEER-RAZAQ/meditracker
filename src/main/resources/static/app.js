const state = {
    rfid: "",
    patientId: null,
    visitId: null,
    lastLabTestId: null,
};

const $ = (id) => document.getElementById(id);
const setText = (id, text, cls = "muted") => {
    const el = $(id);
    el.textContent = text;
    el.className = cls;
};

async function api(path, method = "GET", body) {
    const res = await fetch(path, {
        method,
        headers: body ? { "Content-Type": "application/json" } : undefined,
        body: body ? JSON.stringify(body) : undefined,
    });
    if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || res.statusText);
    }
    const contentType = res.headers.get("content-type") || "";
    return contentType.includes("application/json") ? res.json() : res.text();
}

function getNumber(id) {
    const v = $(id).value.trim();
    return v ? Number(v) : null;
}

async function register() {
    state.rfid = $("rfid").value.trim();
    const fullName = $("fullName").value.trim() || "Demo Patient";
    if (!state.rfid) return setText("regResult", "Enter RFID UID", "err");
    try {
        const patient = await api("/api/registration", "POST", {
            rfidUid: state.rfid,
            fullName,
        });
        state.patientId = patient.id;
        setText("regResult", `Registered patientId=${patient.id}`, "ok");
    } catch (e) {
        setText("regResult", e.message, "err");
    }
}

async function startVisit() {
    if (!state.rfid) return setText("visitResult", "Register first", "err");
    const department = $("department").value;
    try {
        const visit = await api("/api/visits/start", "POST", {
            rfidUid: state.rfid,
            department,
        });
        state.visitId = visit.id;
        setText("visitResult", `Visit started id=${visit.id}`, "ok");
    } catch (e) {
        setText("visitResult", e.message, "err");
    }
}

async function recordVitals() {
    if (!state.visitId) return setText("vitalsResult", "Start visit first", "err");
    const body = {
        temperatureCelsius: getNumber("temp"),
        bpSystolic: getNumber("bpSys"),
        bpDiastolic: getNumber("bpDia"),
        heartRate: getNumber("hr"),
    };
    try {
        const visit = await api(`/api/visits/${state.visitId}/vitals`, "POST", body);
        setText("vitalsResult", `Vitals saved for visit=${visit.id}`, "ok");
    } catch (e) {
        setText("vitalsResult", e.message, "err");
    }
}

async function consult() {
    if (!state.visitId) return setText("consultResult", "Start visit first", "err");
    const body = {
        diagnosis: $("diagnosis").value.trim() || "OK",
        medications: $("meds").value.trim() || "N/A",
        testsNeeded: $("needsTests").checked,
    };
    try {
        await api(`/api/visits/${state.visitId}/consultation`, "POST", body);
        setText("consultResult", `Consultation saved. testsNeeded=${body.testsNeeded}`, "ok");
    } catch (e) {
        setText("consultResult", e.message, "err");
    }
}

async function orderTest() {
    if (!state.visitId) return setText("labResult", "Start visit first", "err");
    const testName = $("testName").value.trim() || "CBC";
    const price = Number($("testPrice").value || "150");
    try {
        const test = await api(`/api/lab/${state.visitId}/order`, "POST", { testName, price });
        state.lastLabTestId = test.id;
        setText("labResult", `Ordered test id=${test.id}`, "ok");
    } catch (e) {
        setText("labResult", e.message, "err");
    }
}

async function completeTest() {
    if (!state.lastLabTestId) return setText("labResult", "Order a test first", "err");
    try {
        const test = await api(`/api/lab/tests/${state.lastLabTestId}/status`, "POST",
            { status: "COMPLETED", resultText: "Normal" });
        setText("labResult", `Completed test id=${test.id}`, "ok");
    } catch (e) {
        setText("labResult", e.message, "err");
    }
}

function renderBills(list) {
    const container = $("bills");
    if (!list || list.length === 0) {
        container.innerHTML = "<p>No bills.</p>";
        return;
    }
    const rows = list.map((b) => `
      <tr>
        <td>${b.id}</td>
        <td>${b.itemDescription}</td>
        <td>${b.type}</td>
        <td>${b.amount}</td>
        <td>${b.status}</td>
        <td>
          <button data-bill="${b.id}" ${b.status === "PAID" ? "disabled" : ""}>Pay</button>
        </td>
      </tr>`).join("");
    container.innerHTML = `
    <table>
      <thead><tr>
        <th>ID</th><th>Item</th><th>Type</th><th>Amount</th><th>Status</th><th>Action</th>
      </tr></thead>
      <tbody>${rows}</tbody>
    </table>`;
    container.querySelectorAll("button[data-bill]").forEach((btn) => {
        btn.addEventListener("click", () => payBill(Number(btn.dataset.bill)));
    });
}

async function refreshBills() {
    if (!state.visitId) return ($("bills").innerHTML = "<p>Start visit first</p>");
    try {
        const bills = await api(`/api/billing/visit/${state.visitId}`, "GET");
        renderBills(bills);
    } catch (e) {
        $("bills").innerHTML = `<p class="err">${e.message}</p>`;
    }
}

async function payBill(billingId) {
    if (!state.rfid) return setText("status", "Register first", "err");
    try {
        await api("/api/billing/pay", "POST", { rfidUid: state.rfid, billingId });
        setText("status", `Paid bill ${billingId}`, "ok");
        refreshBills();
    } catch (e) {
        setText("status", e.message, "err");
    }
}

$("btnRegister").addEventListener("click", register);
$("btnStartVisit").addEventListener("click", startVisit);
$("btnVitals").addEventListener("click", recordVitals);
$("btnConsult").addEventListener("click", consult);
$("btnOrderTest").addEventListener("click", orderTest);
$("btnCompleteTest").addEventListener("click", completeTest);
$("btnRefreshBills").addEventListener("click", refreshBills);