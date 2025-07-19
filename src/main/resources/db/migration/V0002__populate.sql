INSERT INTO users (id, name, email, password, calories_per_day)
VALUES (1000, 'User', 'user@gmail.com', '{noop}password', 2005),
       (1001, 'Admin', 'admin@gmail.com', '{noop}admin', 1900),
       (1002, 'Guest', 'guest@gmail.com', '{noop}guest', 2000);

INSERT INTO meal(user_id, date_time, description, calories)
VALUES
-- 2025-07-18
(1000, '2025-07-18 08:15:00', 'Oatmeal with banana', 500),
(1000, '2025-07-18 16:45:00', 'Yogurt', 1000),
(1000, '2025-07-18 19:10:00', 'BBQ and potatoes', 500),

-- 2025-07-19 exceeded
(1000, '2025-07-19 07:30:00', 'Fried eggs with bread', 310),
(1000, '2025-07-19 11:50:00', 'Burger', 800),
(1000, '2025-07-19 14:20:00', 'Milkshake', 300),
(1000, '2025-07-19 20:00:00', 'Pizza', 700),

-- 2025-07-19 exceeded
(1001, '2025-07-19 08:45:00', 'Muesli with milk', 410),
(1001, '2025-07-19 12:45:00', 'Pilaf', 800),
(1001, '2025-07-19 18:45:00', 'Muffin', 700);


