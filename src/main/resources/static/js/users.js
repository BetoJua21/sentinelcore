
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("userForm");
    const message = document.getElementById("userMessage");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const user = {
            name: document.getElementById("name").value.trim(),
            rfidUid: document.getElementById("rfidUid").value.trim(),
            role: document.getElementById("role").value
        };

        try {
            const response = await fetch("/api/users", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            if (!response.ok) {
                throw new Error("No se pudo registrar el usuario");
            }

            window.location.reload();
        } catch (error) {
            message.textContent = error.message;
        }
    });

    async function updateUserStatus(button, action) {
        const userId = button.dataset.userId;
        const activating = action === "activate";

        const confirmation = activating
            ? "¿Deseas activar este usuario?"
            : "¿Deseas desactivar este usuario?";

        if (!confirm(confirmation)) {
            return;
        }

        button.disabled = true;

        try {
            const response = await fetch(
                `/api/users/${encodeURIComponent(userId)}/${action}`,
                {
                    method: "PATCH"
                }
            );

            if (!response.ok) {
                throw new Error(
                    activating
                        ? "No se pudo activar el usuario"
                        : "No se pudo desactivar el usuario"
                );
            }

            window.location.reload();
        } catch (error) {
            message.textContent = error.message;
            button.disabled = false;
        }
    }

    document.querySelectorAll(".deactivate-button").forEach((button) => {
        button.addEventListener("click", () => {
            updateUserStatus(button, "deactivate");
        });
    });

    document.querySelectorAll(".activate-button").forEach((button) => {
        button.addEventListener("click", () => {
            updateUserStatus(button, "activate");
        });
    });
});
