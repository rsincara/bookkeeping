window.onload = function () {

    let store = {
        resultInfo: {},
        chosenBalance: null,
    };

    $.get("/get-user-info", {

        userName: document.getElementById('username').innerText
    }).done((result) => {
        store.resultInfo = result;
        store.chosenBalance = result.balancesWithTransactions[0];
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
        store.chosenBalance = store.resultInfo.balancesWithTransactions.find((item) => item.balanceName === selected);
        setBalanceAndTransactionInfo(store.chosenBalance);
        console.log('selected', selected);
        console.log('store', store);
    });

    $('#add-balance-close-modal').click(() => {
       document.getElementById('add-modal').style.display = 'none';
    });

    $('#add-balance-btn').click(() => {
        document.getElementById('add-modal').style.display = 'flex';
    });

    $('#remove-balance-btn').click(() => {
        $.get("/remove-balance", {
            userName: document.getElementById('username').innerText,
            balanceName: store.chosenBalance.balanceName
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
                    </div>
                `;
            if (transaction.transactionType === 'income') {
                incomeContent.append(innerHtml);
            } else {
                consumptionContent.append(innerHtml);
            }
        })
    }

    // $(".remove-link").click((e) => {
    //     e.currentTarget.closest('.content-wrapper').remove();
    //     var btn = $(e.currentTarget);
    //     var purchaseID = btn.attr("data-delete-id");
    //     $.post("/purchase/" + purchaseID + "/delete", () => {});
    // });
    //
    // $(".checkbox").click((e) => {
    //     var btn = $(e.currentTarget);
    //     var purchaseID = btn.attr("data-purchase-id");
    //     btn.attr("disabled", "disabled");
    //     $.post("/purchase/" + purchaseID + "/bought", () => {});
    // });
};