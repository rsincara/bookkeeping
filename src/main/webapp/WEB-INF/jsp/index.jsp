<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
  <title>Главная</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="../../resources/css/style.css">
  <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
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
        <a href="#" class="balance__new-balance">Добавить счет</a> <br/>
        <a href="#" class="balance__new-transaction">Добавить транзакцию</a>
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
    </div>
  </main>
</div>
  </sec:authorize>
</div>
</body>
</html>