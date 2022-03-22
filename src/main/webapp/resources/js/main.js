Date.prototype.toDateInputValue = (function () {
    let local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});

const labels = [];

const data = {
    labels: labels,
    datasets: [{
        label: 'Изменение бюджета по дням',
        backgroundColor: 'rgb(255, 99, 132)',
        borderColor: 'rgb(255, 99, 132)',
        data: [],
    }]
};

const config = {
    type: 'line',
    data: data,
    options: {}
};

function setDataOnChart(balanceOnDates) {
    balanceOnDates.forEach((balance) => {
        labels.push(balance.date);
        data.datasets[0].data.push(balance.amount);
    });
}

function openChangeTransactionModal(transaction) {
    document.getElementById('change-transaction-modal').style.display = 'flex';
    document.getElementById('transactionId').value = transaction.id;
    document.getElementById('change-transaction-transaction-summ').value = transaction.amount;
    document.getElementById('change-transaction-transaction-date').value = transaction.date;
    document.getElementById('change-transaction-transaction-commentary').value = transaction.commentary
    const select = [...document.querySelector('#change-transaction-transaction-type').getElementsByTagName('option')];
    select.forEach((selectItem) => {
        if (selectItem.innerText === 'Доход' && transaction.transactionType === 'income' ||
            selectItem.innerText === 'Расход' && transaction.transactionType === 'consumption') {
            selectItem.selected = true;
        }
    })
}

function removeTransactionRequest(transactionId) {
    $.ajax( {
        url: "/transactions",
        method: 'DELETE',
        data: {
            id: transactionId
        }
    }).done(() => {
        location.reload();
    });
}

window.onload = function () {
    let store = {
        resultInfo: {},
        chosenBalance: null,
    };

    document.getElementById('transaction-date').value = new Date().toDateInputValue();

    $.get(`/user/${document.getElementById('username').innerText}/info`).done((result) => {
        setDataOnChart(result.balanceOnDates);
        const myChart = new Chart(
            document.getElementById('myChart'),
            config
        );
        store.resultInfo = result;
        store.chosenBalance = result.balancesWithTransactions[0];
        document.getElementById('transaction-name').innerText = result.balancesWithTransactions[0].balanceName;
        document.getElementById('transaction-balance-name').value = result.balancesWithTransactions[0].balanceName;
        document.getElementById('old-balance-name').value = result.balancesWithTransactions[0].balanceName;
        setBalanceAndTransactionInfo(store.chosenBalance);
        document.getElementById('general-balance__value').innerText = `${result.generalBalance}р`;
        result.balancesWithTransactions.forEach((balanceWithTransactions) => {
            const balanceName = balanceWithTransactions.balanceName;
            $('#balance-select').append(`
                <option value='${balanceName}'>
                    ${balanceName}
                </option>
            `)
        })

        $('#balance-select').append(``);
        console.log(result);
    });

    $('#balance-select').change(() => {
        const selected = $("#balance-select").val();
        document.getElementById('transaction-name').innerText = selected;
        document.getElementById('transaction-balance-name').value = selected;
        document.getElementById('old-balance-name').value = selected;

        store.chosenBalance = store.resultInfo.balancesWithTransactions.find((item) => item.balanceName === selected);
        setBalanceAndTransactionInfo(store.chosenBalance);
        console.log('selected', selected);
        console.log('store', store);
    });

    $('.add-balance__close').click(() => {
        document.getElementById('add-modal').style.display = 'none';
        document.getElementById('add-transaction-modal').style.display = 'none';
        document.getElementById('change-transaction-modal').style.display = 'none';
        document.getElementById('update-balance-modal').style.display = 'none';
    });

    $('#add-balance-btn').click(() => {
        document.getElementById('add-modal').style.display = 'flex';
    });

    $('#add-transaction-btn').click(() => {
        document.getElementById('add-transaction-modal').style.display = 'flex';
    });

    $('#update-balance-btn').click(() => {
        document.getElementById('update-balance-modal').style.display = 'flex';
    });


    // balance requests
    $('#remove-balance-btn').click(() => {
        $.ajax( {
            url: `/balance/${store.chosenBalance.balanceId}`,
            method: 'DELETE',
        }).done(() => {
            location.reload();
        });
    });
    $('#add-balance__submit-btn').click(() => {
        $.ajax( {
            url: "/balance",
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify({
                userName: document.getElementById('username').innerText,
                balanceName: document.getElementById('balance-name').value,
                balanceAmount: document.getElementById('start-balance').value,
            })
        }).done(() => {
            location.reload();
        });
    });

    $('#update-balance-apply-btn').click(() => {
        $.ajax( {
            url: "/balance",
            method: 'PUT',
            data: {
                userName: document.getElementById('username').innerText,
                balanceId: store.chosenBalance.balanceId,
                newBalanceName: document.getElementById('new-balance-name').value,
            }
        }).done(() => {
            location.reload();
        });
    });

    // transaction requests
    $('#add-new-transaction-btn').click(() => {
        $.ajax( {
            url: "/transactions",
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify({
                userName: document.getElementById('username').innerText,
                balanceId: store.chosenBalance.balanceId,
                amount: document.getElementById('transaction-summ').value,
                date: document.getElementById('transaction-date').value,
                transactionType: document.getElementById('transaction-type').value,
                commentary: document.getElementById('transaction-commentary').value,
            })
        }).done(() => {
            location.reload();
        });
    });
    $('#update-transaction-apply-btn').click(() => {
        $.ajax( {
            url: "/transactions",
            method: 'PUT',
            data: {
                transactionId: document.getElementById('transactionId').value,
                transactionType: document.getElementById('change-transaction-transaction-type').value,
                amount: document.getElementById('change-transaction-transaction-summ').value,
                date: document.getElementById('change-transaction-transaction-date').value,
                commentary: document.getElementById('change-transaction-transaction-commentary').value,
            }
        }).done(() => {
            location.reload();
        });
    });


    function setBalanceAndTransactionInfo(chosenBalance) {
        document.getElementById('current-balance__value').innerText = `${chosenBalance.amount}р`
        const incomeContent = $('#transactions__income-content');
        const consumptionContent = $('#transactions__consumption-content');
        incomeContent.empty();
        consumptionContent.empty();
        store.chosenBalance.transactions.forEach((transaction) => {
            const innerHtml = `
                     <div class="transactions__data-row">
                        <div class="transactions__date">${transaction.date}</div>
                        <div class="transactions__amount">${transaction.amount}</div>
                        <div class="transactions__comment">${transaction.commentary}</div>
                        <a class="transactions__change" onclick='openChangeTransactionModal(${JSON.stringify(transaction)})'>Изменить</a>
                        <a onclick='removeTransactionRequest(${transaction.id})' data-transaction-id="${transaction.id}" class="transactions__remove">Удалить</a>
                    </div>
                `;
            if (transaction.transactionType === 'income') {
                incomeContent.append(innerHtml);
            } else {
                consumptionContent.append(innerHtml);
            }
        })
    }
};