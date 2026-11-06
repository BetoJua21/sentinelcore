
document.addEventListener("DOMContentLoaded", () => {
    const button = document.getElementById("readRfidButton");
    const status = document.getElementById("rfidStatus");
    const input = document.getElementById("rfidUid");

    if (!button || !status || !input) return;

    const wait = ms => new Promise(resolve => setTimeout(resolve, ms));

    button.addEventListener("click", async () => {
        button.disabled = true;
        status.textContent = "Esperando tarjeta RFID...";

        try {
            const startResponse = await fetch(
                "/api/rfid/registration/start",
                { method: "POST" }
            );

            if (!startResponse.ok) {
                throw new Error("No se pudo iniciar la lectura");
            }

            const { sessionId } = await startResponse.json();

            let captured = false;

            for (let attempt = 0; attempt < 30; attempt++) {
                await wait(2000);

                const response = await fetch(
                    `/api/rfid/registration/status?sessionId=${encodeURIComponent(sessionId)}`
                );

                if (!response.ok) break;

                const result = await response.json();

                if (result.status === "CAPTURED") {
                    input.value = result.uid;
                    status.textContent = "Tarjeta detectada correctamente";
                    captured = true;
                    break;
                }
            }

            if (!captured) {
                status.textContent = "Tiempo de lectura agotado";
            }
        } catch (error) {
            status.textContent = error.message;
        } finally {
            button.disabled = false;
        }
    });
});
