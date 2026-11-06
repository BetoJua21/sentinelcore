
document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("accessChart");
    const dataElement = document.getElementById("accessChartData");

    if (!canvas || !dataElement || typeof Chart === "undefined") {
        return;
    }

    const parseList = value => {
        if (!value || value === "[]") {
            return [];
        }

        return value
            .replace(/^\[|\]$/g, "")
            .split(",")
            .map(item => item.trim());
    };

    const labels = parseList(dataElement.dataset.labels);
    const dates = parseList(dataElement.dataset.dates);
    const granted = parseList(dataElement.dataset.granted).map(Number);
    const denied = parseList(dataElement.dataset.denied).map(Number);

    new Chart(canvas, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [
                {
                    label: "Concedidos",
                    data: granted,
                    backgroundColor: "#14b8a6",
                    borderRadius: 5,
                    maxBarThickness: 30
                },
                {
                    label: "Denegados",
                    data: denied,
                    backgroundColor: "#f87171",
                    borderRadius: 5,
                    maxBarThickness: 30
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: "nearest",
                intersect: true
            },
            onClick: (event, elements) => {
                if (elements.length === 0) {
                    return;
                }

                const selected = elements[0];
                const date = dates[selected.index];
                const grantedValue = selected.datasetIndex === 0;

                const params = new URLSearchParams({
                    date: date,
                    granted: grantedValue
                });

                window.location.href = `/accesses?${params.toString()}`;
            },
            scales: {
                x: {
                    grid: {
                        display: false
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            },
            plugins: {
                legend: {
                    position: "bottom",
                    labels: {
                        usePointStyle: true,
                        boxWidth: 10,
                        padding: 20
                    }
                },
                tooltip: {
                    callbacks: {
                        title: items => dates[items[0].dataIndex]
                    }
                }
            }
        }
    });

    canvas.style.cursor = "pointer";
});
