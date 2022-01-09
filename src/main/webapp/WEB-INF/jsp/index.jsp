<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
  <title>Главная</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="../../resources/css/style.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Inter&display=swap" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="../../resources/js/main.js"></script>

</head>
<body>
<div>
  <sec:authorize access="!isAuthenticated()">
    <h4><a href="/login">Войти</a></h4>
    <h4><a href="/registration">Зарегистрироваться</a></h4>
  </sec:authorize>
  <sec:authorize access="isAuthenticated()">
    <header class="header">
      <div class="content-wrapper">
        <div class="authorized-flex">
          <h3 id="username" class="authorized-username">${pageContext.request.userPrincipal.name}</h3>
          <a class="logout-btn" href="/logout">Выйти</a>
        </div>
      </div>
    </header>
  </sec:authorize>
  <sec:authorize access="isAuthenticated()">

  <div class="modal" style="display: none" id="update-balance-modal">
    <div class="add-balance">
      <h2 class="add-balance__title">Изменить имя счета</h2>
      <button class="add-balance__close">
        &#10006;
      </button>
      <form class="add-balance__form" method="post" action="/update-balance">
        <input hidden name="userName" value="${pageContext.request.userPrincipal.name}" />
        <div class="add-balance__input-wrapper">
          <div class="add-balance__label-wrapper">
            <input id="old-balance-name" name="oldBalanceName" hidden>
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="new-balance-name">Новое название</label>
            <input id="new-balance-name" name="newBalanceName">
          </div>
        </div>

        <button class="add-balance__submit-btn" type="submit">Сохранить</button>
      </form>
    </div>
  </div>

  <div class="modal" style="display: none" id="change-transaction-modal">
    <div class="add-balance">
      <h2 class="add-balance__title">Изменить транзакцию</h2>
      <button class="add-balance__close">
        &#10006;
      </button>
      <form class="add-balance__form" method="post" action="/update-transaction">
        <input hidden name="transactionId" id="transactionId" />

        <div class="add-balance__input-wrapper">
          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-type">Тип транзакции</label>
            <select id="change-transaction-transaction-type" name="transactionType">
              <option>Доход</option>
              <option>Расход</option>
            </select>
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-summ">Сумма</label>
            <input id="change-transaction-transaction-summ" name="amount">
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-date">Дата</label>
            <input id="change-transaction-transaction-date" type="date" name="date">
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-commentary">Комментарий</label>
            <textarea class="add-balance__textarea" id="change-transaction-transaction-commentary" name="commentary"> </textarea>
          </div>

        </div>

        <button class="add-balance__submit-btn" type="submit">Сохранить</button>
      </form>
    </div>
  </div>

  <div class="modal" style="display: none" id="add-transaction-modal">
    <div class="add-balance">
      <h2 class="add-balance__title">Добавить транзакцию к счету</h2>
      <h2 class="add-balance__title" id="transaction-name"></h2>
      <button class="add-balance__close">
        &#10006;
      </button>
      <form class="add-balance__form" method="post" action="/add-transaction">
        <input hidden name="userName" value="${pageContext.request.userPrincipal.name}" />
        <input hidden name="balanceName" id="transaction-balance-name" />

        <div class="add-balance__input-wrapper">
          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-type">Тип транзакции</label>
            <select id="transaction-type" name="transactionType">
              <option>Доход</option>
              <option>Расход</option>
            </select>
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-summ">Сумма</label>
            <input id="transaction-summ" name="amount">
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-date">Дата</label>
            <input id="transaction-date" type="date" name="date">
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="transaction-commentary">Комментарий</label>
            <textarea class="add-balance__textarea" id="transaction-commentary" name="commentary"> </textarea>
          </div>

        </div>

        <button class="add-balance__submit-btn" type="submit">Сохранить</button>
      </form>
    </div>
  </div>

  <div class="modal" id="add-modal" style="display: none">
    <div class="add-balance">
      <h2 class="add-balance__title">Добавить счет</h2>
      <button class="add-balance__close">
        &#10006;
      </button>
      <form class="add-balance__form" method="post" action="/add-balance">
        <div class="add-balance__input-wrapper">
          <input hidden name="userName" value="${pageContext.request.userPrincipal.name}" />
          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="balance-name">Название счета</label>
            <input id="balance-name" name="balanceName">
          </div>

          <div class="add-balance__label-wrapper">
            <label class="add-balance__label" for="start-balance">Стартовый капитал</label>
            <input id="start-balance" name="balanceAmount">
          </div>
        </div>

        <button class="add-balance__submit-btn" type="submit">Сохранить</button>
      </form>
    </div>
  </div>

  <div class="content-wrapper">

    <div class="general-balance">
      <p class="general-balance__text">Общий баланс: <span id="general-balance__value"></span> </p>
    </div>
  </div>
  <main class="main">
    <div class="content-wrapper">

      <div class="balance">
        <label>
          Счет:
          <select id="balance-select">
          </select>
        </label>
        <p class="balance__current-balance">
          Текущий баланс: <span id="current-balance__value"></span>
        </p>
        <a href="#" class="balance__change-balance" id="update-balance-btn">Изменить имя счета</a> <br/>
        <a href="#" class="balance__new-balance" id="add-balance-btn">Добавить счет</a> <br/>
        <a href="#" class="balance__remove-balance" id="remove-balance-btn">Удалить счет</a> <br/>
        <a href="#" class="balance__new-transaction" id="add-transaction-btn">Добавить транзакцию</a>
      </div>

      <div class="transactions">
        <div class="transactions__income">
          <h2 class="transactions__title">Доходы</h2>
          <div class="transactions__data-row">
            <div class="transactions__date">Дата</div>
            <div class="transactions__amount">Сумма</div>
            <div class="transactions__comment">Комментарий</div>
          </div>
          <div id="transactions__income-content">
          </div>
        </div>
        <div class="transactions__consumption">
          <h2 class="transactions__title">Расходы</h2>
          <div class="transactions__data-row">
            <div class="transactions__date">Дата</div>
            <div class="transactions__amount">Сумма</div>
            <div class="transactions__comment">Комментарий</div>
          </div>
          <div id="transactions__consumption-content">
          </div>
        </div>
      </div>

      <div>
        <canvas id="myChart"></canvas>
      </div>

    </div>
  </main>
</div>
  </sec:authorize>
</div>
</body>
</html>