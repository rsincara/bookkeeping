delete
from t_user;

insert into t_user(id, password, username)
values (1, '$2a$10$uvzgAvaojkgXRd6Gq/B10O7BtlVsRSLx/KOPx0LynBrritEIeYCCW', 'test');

insert into t_balances(id, amount, name, user_id)
values (1, '100', 'test visa', 1);
