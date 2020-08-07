DROP VIEW ПредставлениеРейсов
GO

CREATE VIEW ПредставлениеРейсов AS
SELECT Рейс.Код, Дата, Номер_Поезда[Номер Поезда]
FROM Рейс, Маршрут
WHERE Маршрут.Код = Рейс.Код_Маршрута
GO
/*
SELECT * FROM ПредставлениеРейсов
GO
*/
DROP VIEW ПредставлениеРаботников
GO

DROP VIEW ПредставлениеБилетов
GO

CREATE VIEW ПредставлениеБилетов AS
SELECT Билеты.Код, Дата, [Номер Поезда], [Номер Вагона], Номер_Места, [Тип Вагона], Цена, ФИО, стотпр.Название [Станция Отправления], стпр.Название [Станция Прибытия]
FROM ПредставлениеВагонов, Станция стотпр, Станция стпр, Билеты
WHERE Код_Станции_Отправления = стотпр.Код AND Код_Станции_Прибытия = стпр.Код AND ПредставлениеВагонов.Код = Код_Вагона
GO
/*
SELECT * FROM ПредставлениеБилетов
GO
*/
CREATE VIEW ПредставлениеРаботников AS
SELECT Работники.Код, ФИО, Должность
FROM Работники, Должность
WHERE Должность.Код = Работники.Код_Должности
GO

DROP VIEW ПредставлениеСтанцийНаМаршруте
GO

CREATE VIEW ПредставлениеСтанцийНаМаршруте AS
SELECT Станции_На_Маршруте.Код, Номер_Поезда [Номер Поезда], Название[Станция], Время_Прибытия[Время Прибытия], Время_Отправления[Время Отправления], Номер_На_Маршруте[Номер На Маршруте], Тип
FROM Маршрут, Станция, Станции_На_Маршруте
WHERE Маршрут.Код = Код_Маршрута AND Станция.Код = Код_Станции
GO
/*
SELECT * FROM ПредставлениеСтанцийНаМаршруте
GO
*/
DROP VIEW ПредставлениеРаботниковВРейсе
GO

CREATE VIEW ПредставлениеРаботниковВРейсе AS
SELECT Работники_В_Рейсе.Код, ФИО, Должность, Дата, [Номер Поезда]
FROM Работники_В_Рейсе, ПредставлениеРаботников, ПредставлениеРейсов
WHERE Код_Работника = ПредставлениеРаботников.Код AND Код_Рейса = ПредставлениеРейсов.Код 
GO

SELECT * FROM ПредставлениеРаботниковВРейсе
GO

/*
SELECT * FROM ПредставлениеРаботников
GO
*/

/*
SELECT * FROM Работники_В_Рейсе, ПредставлениеРейсов WHERE Дата > '2020-05-06' AND
	Код_Работника = 2 AND ПредставлениеРейсов.Код = Код_Рейса
GO
*/
DROP TRIGGER trg_СтанцияУдаление
GO

--Триггер выполняющий каскадное удаление записей из таблицы "Станция"
CREATE TRIGGER trg_СтанцияУдаление ON Станция
	AFTER DELETE
AS
DELETE FROM Станции_На_Маршруте
WHERE Код_Станции IN (SELECT Код FROM deleted)
DELETE FROM Билеты
WHERE Код_Станции_Отправления IN (SELECT Код FROM deleted) OR Код_Станции_Прибытия IN (SELECT Код FROM deleted)
GO

DROP TRIGGER trg_ТипыВагоновУдаление
GO

--Триггер выполняющий каскадное удаление записей из таблицы "Типы_Вагонов"
CREATE TRIGGER trg_ТипыВагоновУдаление ON Типы_Вагонов
	AFTER DELETE
AS
DELETE FROM Билеты
WHERE Код_Вагона IN (SELECT Вагоны.Код FROM Вагоны, deleted WHERE Код_Типа_Вагона = deleted.Код)
DELETE FROM Вагоны
WHERE Код_Типа_Вагона IN (SELECT Код FROM deleted)
GO

DROP TRIGGER trg_Вагоны 
GO

--триггер делает проверку на существование билетов на вагон, подлежащий удалению
CREATE TRIGGER trg_Вагоны ON Вагоны
	AFTER DELETE
AS
IF EXISTS(SELECT * FROM Билеты WHERE Код_Вагона IN (SELECT Код FROM deleted))
BEGIN
	RAISERROR('Нельзя удалить вагон, потому что на него есть билеты!', 16, 2)
	ROLLBACK
END
GO

DROP TRIGGER trg_Рейс_Добавление
GO

CREATE TRIGGER trg_Рейс_Добавление ON Рейс
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Маршрут WHERE Маршрут.Код = Код_Маршрута)
BEGIN
	RAISERROR('Нельзя добавить рейс на несуществующий маршрут!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_Билеты_Добавление_ПроверкаВагона
GO

CREATE TRIGGER trg_Билеты_Добавление_ПроверкаВагона ON Билеты
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Вагоны WHERE Вагоны.Код = Код_Вагона)
BEGIN
	RAISERROR('Нельзя добавить билет на несуществующий вагон!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_Билеты_Добавление_ПроверкаСтОтпр
GO

CREATE TRIGGER trg_Билеты_Добавление_ПроверкаСтОтпр ON Билеты
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Станция WHERE Станция.Код = Код_Станции_Отправления)
BEGIN
	RAISERROR('Нельзя добавить билет на несуществующую станцию отправления!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_Билеты_Добавление_ПроверкаСтПР
GO

CREATE TRIGGER trg_Билеты_Добавление_ПроверкаСтПР ON Билеты
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Станция WHERE Станция.Код = Код_Станции_Прибытия)
BEGIN
	RAISERROR('Нельзя добавить билет на несуществующую станцию прибытия!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_Вагоны_Изменение
GO

CREATE TRIGGER trg_Вагоны_Изменение ON Вагоны
	AFTER UPDATE
AS
IF UPDATE(Код_Типа_Вагона)
	--IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Типы_Вагонов WHERE Типы_Вагонов.Код = Код_Типа_Вагона)
	IF ((SELECT TOP 1 inserted.Код_Типа_Вагона FROM inserted, deleted WHERE inserted.Код = deleted.Код
		AND inserted.Код_Типа_Вагона <> deleted.Код_Типа_Вагона) NOT IN (SELECT Код FROM Типы_Вагонов))
	BEGIN
		RAISERROR('Нельзя изменить вагон на вагон несуществующего типа!', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_СтанцииНаМаршруте_ИзменениеСтанции 
GO

CREATE TRIGGER trg_СтанцииНаМаршруте_ИзменениеСтанции ON Станции_На_Маршруте
	AFTER UPDATE
AS
IF UPDATE(Код_Станции)
	IF ((SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, Станция WHERE Станция.Код = Код_Станции)) 
	BEGIN
		RAISERROR('Нельзя изменить станцию на маршруте на несуществующую станцию!', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_Рейс_ИзменениеДаты
GO

--Триггер проверяющий дату на устарелость(нельзя хранить рейсы до 1991 года) после редактирования таблицы
CREATE TRIGGER trg_Рейс_ИзменениеДаты ON Рейс
	AFTER UPDATE
AS
IF UPDATE(Дата)
	IF ((SELECT COUNT(*) FROM inserted WHERE Дата > '1991-01-01') <> (SELECT COUNT(*) FROM deleted))
	BEGIN
		RAISERROR('В базе данных нельзя хранить данные рейсов, которые были раньше 1991 года', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_Рейс_ДобавлениеДаты
GO

--Триггер проверяющий новые записи в таблицу "Рейс" на устарелость(нельзя хранить рейсо до 1991 года) 
CREATE TRIGGER trg_Рейс_ДобавлениеДаты ON Рейс
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted WHERE Дата > '1991-01-01')
BEGIN
	RAISERROR('Нельзя добавить рейс раньше 1991 года', 16, 1)
	ROLLBACK
END
GO
--Добавляется после инициализации всех таблиц
ALTER TABLE Маршрут 
	ADD КоличествоСтанций INT NOT NULL DEFAULT 0
GO

UPDATE Маршрут SET КоличествоСтанций = (SELECT COUNT(*) FROM Станции_На_Маршруте WHERE Станции_На_Маршруте.Код_Маршрута = Маршрут.Код)
GO
/*
SELECT * FROM Маршрут
GO
*/
DROP TRIGGER ОбновлениеМаршрута
GO

--Триггер для обновления числового поля "КоличествоСтанций" таблицы "Маршрут"
CREATE TRIGGER ОбновлениеМаршрута ON Станции_На_Маршруте
	AFTER INSERT, DELETE, UPDATE
AS
UPDATE Маршрут SET КоличествоСтанций += Кол
FROM (SELECT Код_Маршрута, COUNT(*) Кол FROM inserted GROUP BY Код_Маршрута) ins
WHERE Маршрут.Код = Код_Маршрута
UPDATE Маршрут SET КоличествоСтанций -= Кол	
FROM (SELECT Код_Маршрута, COUNT(*) Кол FROM deleted GROUP BY Код_Маршрута) del
WHERE Маршрут.Код = Код_Маршрута
GO

DROP VIEW ПредставлениеВагонов
GO

CREATE VIEW ПредставлениеВагонов AS
SELECT Вагоны.Код, Дата, Номер_Поезда[Номер Поезда], Тип_Вагона[Тип Вагона], Номер[Номер Вагона]
FROM Вагоны, Рейс, Маршрут, Типы_Вагонов
WHERE Код_Рейса = Рейс.Код AND Маршрут.Код = Код_Маршрута AND Типы_Вагонов.Код = Код_Типа_Вагона
GO

DROP TRIGGER ВагоныДобавление
GO

--Триггер для добавления записей в таблицу "Вагоны" через представление
CREATE TRIGGER ВагоныДобавление ON ПредставлениеВагонов
	INSTEAD OF INSERT
AS
INSERT INTO Вагоны(Код_Рейса, Код_Типа_Вагона, Номер)
	SELECT Рейс.Код, Типы_Вагонов.Код, [Номер Вагона]
	FROM inserted INNER JOIN Рейс ON Рейс.Дата = inserted.Дата
	INNER JOIN Маршрут ON Маршрут.Номер_Поезда = inserted.[Номер Поезда] AND Маршрут.Код = Рейс.Код_Маршрута
	INNER JOIN Типы_Вагонов ON Типы_Вагонов.Тип_Вагона = inserted.[Тип Вагона]
	WHERE inserted.Дата IS NOT NULL AND inserted.[Номер Поезда] IS NOT NULL AND inserted.[Номер Вагона] IS NOT NULL
		AND inserted.[Тип Вагона] IS NOT NULL
PRINT('Adding information')
GO

/*
INSERT INTO ПредставлениеВагонов (Дата, [Номер Поезда], [Тип Вагона], [Номер Вагона]) VALUES ('2018-10-05', 704, 'Плацкарт', 33)
GO
*/
DROP TRIGGER ВагоныПравка
GO

--Триггер для редактирования записей таблицы "Вагоны" через представление
CREATE TRIGGER ВагоныПравка ON ПредставлениеВагонов
	INSTEAD OF UPDATE
AS
IF NOT UPDATE (Дата) AND NOT UPDATE ([Номер Поезда])
	UPDATE Вагоны SET Код_Типа_Вагона = Типы_Вагонов.Код, Номер = inserted.[Номер Вагона]
	FROM Вагоны Ваг INNER JOIN inserted ON inserted.Код = Ваг.Код
		INNER JOIN Типы_Вагонов ON Типы_Вагонов.Тип_Вагона = inserted.[Тип Вагона]
	WHERE inserted.[Номер Вагона] IS NOT NULL AND inserted.[Тип Вагона] IS NOT NULL
ELSE IF NOT UPDATE ([Тип Вагона])
	UPDATE Вагоны SET Код_Рейса = Рейс.Код, Номер = inserted.[Номер Вагона]
	FROM Вагоны Ваг INNER JOIN inserted ON inserted.Код = Ваг.Код
		INNER JOIN Рейс ON Рейс.Дата = inserted.Дата
		INNER JOIN Маршрут ON Маршрут.Номер_Поезда = inserted.[Номер Поезда] AND Маршрут.Код = Рейс.Код_Маршрута
	WHERE inserted.Дата IS NOT NULL AND inserted.[Номер Поезда] IS NOT NULL AND inserted.[Номер Вагона] IS NOT NULL
ELSE
	UPDATE Вагоны SET Код_Рейса = Рейс.Код, Код_Типа_Вагона = Типы_Вагонов.Код, Номер = inserted.[Номер Вагона]
	FROM Вагоны Ваг INNER JOIN inserted ON inserted.Код = Ваг.Код
		INNER JOIN Рейс ON Рейс.Дата = inserted.Дата
		INNER JOIN Маршрут ON Маршрут.Номер_Поезда = inserted.[Номер Поезда] AND Маршрут.Код = Рейс.Код_Маршрута
		INNER JOIN Типы_Вагонов ON Типы_Вагонов.Тип_Вагона = inserted.[Тип Вагона]
	WHERE inserted.Дата IS NOT NULL AND inserted.[Номер Поезда] IS NOT NULL AND inserted.[Номер Вагона] IS NOT NULL
		AND inserted.[Тип Вагона] IS NOT NULL
	PRINT('Updating')
GO
/*
UPDATE ПредставлениеВагонов SET Дата = '2018-10-05', [Номер Поезда] = 704, [Тип Вагона] = 'Плацкарт', [Номер Вагона] = 111
WHERE Код = 2
GO
*/
DROP TRIGGER ВагоныУдаление
GO

--Триггер для удаления записей через представление
CREATE TRIGGER ВагоныУдаление ON ПредставлениеВагонов
	INSTEAD OF DELETE
AS
	DELETE FROM	Вагоны WHERE Код IN (SELECT Код FROM deleted)
GO
/*
DELETE FROM ПредставлениеВагонов
WHERE Код = 8
GO
*/

/*
CREATE PROC РасчётЦены @КодВагона INT, @КодСтанцииОтправления INT, @КодСтанцииПрибытия INT
AS
	
GO
*/

DROP FUNCTION СписокРейсов
GO

--параметр - код работника, который получается из фио работника с помощью программного кода.
--результат - список рейсов с дополнительной информацией об обслуженных вагонах на рейсе
CREATE FUNCTION СписокРейсов (@КодРаботника INT)
RETURNS TABLE
AS
RETURN
	SELECT Дата, [Номер Поезда], Номер[Номер Вагона], Тип_Вагона[Тип Вагона]
	FROM Работники_В_Рейсе, ПредставлениеРейсов, Вагоны, Типы_Вагонов
	WHERE @КодРаботника = Код_Работника AND Работники_В_Рейсе.Код_Рейса = ПредставлениеРейсов.Код AND 
		ПредставлениеРейсов.Код = Вагоны.Код_Рейса AND Код_Типа_Вагона = Типы_Вагонов.Код
GO

/*
SELECT * FROM Работники_В_Рейсе

SELECT * FROM СписокРейсов(2)
GO
*/
DROP PROC КоличествоРейсов
GO


CREATE PROC КоличествоРейсов @КодРаботника INT
AS
DECLARE @Количество INT = (SELECT COUNT(*) FROM СписокРейсов(@КодРаботника))
RETURN @Количество
GO

DROP PROC КоличествоПассажиров
GO

--параметр - код работника, который получается из фио работника с помощью программного кода.
--результат - количество обслуженных пассажиров на рейсе, на котором был выбранный работник
CREATE PROC КоличествоПассажиров @КодРаботника INT
AS
DECLARE @Количество INT = (SELECT COUNT(*) FROM Работники_В_Рейсе, Рейс, Вагоны, Билеты
	WHERE @КодРаботника = Код_Работника AND Работники_В_Рейсе.Код_Рейса = Рейс.Код AND Рейс.Код = Вагоны.Код_Рейса
		AND Вагоны.Код = Код_Вагона)
RETURN @Количество
GO

DROP PROC ПоднятьЦену
GO
/*
SELECT * FROM ПредставлениеБилетов
GO
*/

--параметр @ТипВагона - название типа вагона из таблицы "Типы_Вагонов"
--параметр @Процент - коэффициент, на который умножается цена билетов
--результат - изменение цен на билеты с определенным типом вагона и на указанный коэффициент
CREATE PROC ПоднятьЦену @ТипВагона VARCHAR(50), @Процент FLOAT
AS
UPDATE Билеты SET Цена = Цена * @Процент
WHERE Код_Вагона IN (SELECT Код FROM Вагоны WHERE Код_Типа_Вагона = 
	(SELECT Типы_Вагонов.Код FROM Типы_Вагонов WHERE Тип_Вагона = @ТипВагона))
GO
/*
EXEC ПоднятьЦену 'Плацкарт', 1.2
GO
*/
DROP PROC СписокБилетов
GO

--параметр - выбранная дата
--результат - список билетов начиная с определенной даты
CREATE PROC СписокБилетов @Дата DATE
AS
SELECT Дата, Номер_Поезда [Номер Поезда], Номер_Места [Номер Места], Номер [Номер Вагона], Тип_Вагона[Тип Вагона], Цена, ФИО,
	стотпр.Название [Станция Отправления], стпр.Название [Станция Прибытия]
INTO #Список
FROM Билеты INNER JOIN Вагоны ON Вагоны.Код = Код_Вагона
	INNER JOIN Рейс ON Код_Рейса = Рейс.Код
	INNER JOIN Маршрут ON Код_Маршрута = Маршрут.Код
	INNER JOIN Типы_Вагонов ON Типы_Вагонов.Код = Код_Типа_Вагона
	INNER JOIN Станция AS стотпр ON стотпр.Код = Код_Станции_Отправления
	INNER JOIN Станция AS стпр ON стпр.Код = Код_Станции_Прибытия 
WHERE Дата > @Дата
SELECT * FROM #Список
GO

EXEC СписокБилетов '2018-10-04'
GO

DROP PROC СписокВагоновНаМаршрут 
GO
/*
SELECT * FROM Вагоны
GO
*/
--параметр - номер поезда из таблицы Маршрут
--результат - информация о вагонах и сумма проданных билетов на каждый вагон на выбранный маршрут
CREATE PROC СписокВагоновНаМаршрут @НомерПоезда INT
AS
CREATE TABLE #СведенияОВагонах (ДатаРейса DATE, НомерВагона INT, ТипВагона VARCHAR(50), Сумма INT)
DECLARE @КодМаршрута INT = (SELECT Код FROM Маршрут WHERE Номер_Поезда = @НомерПоезда)
DECLARE Cursor1 CURSOR FOR
	SELECT Код, Дата FROM Рейс WHERE @КодМаршрута = Код_Маршрута
OPEN Cursor1
DECLARE @КодРейса INT, @ДатаРейса DATE
FETCH Cursor1 INTO @КодРейса, @ДатаРейса
WHILE (@@FETCH_STATUS = 0)
BEGIN
	DECLARE Cursor2 CURSOR FOR
	SELECT Вагоны.Код, Тип_Вагона, Номер FROM Вагоны, Типы_Вагонов WHERE Код_Рейса = @КодРейса AND Код_Типа_Вагона = Типы_Вагонов.Код
	OPEN Cursor2
	DECLARE @КодВагона INT, @ТипВагона VARCHAR(50), @НомерВагона INT
	FETCH Cursor2 INTO @КодВагона, @ТипВагона, @НомерВагона
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		DECLARE @Сумма INT = (SELECT ISNULL(SUM(Цена), 0) FROM Билеты WHERE Код_Вагона = @КодВагона)
		INSERT INTO #СведенияОВагонах VALUES (@ДатаРейса, @НомерВагона, @ТипВагона, @Сумма)
		FETCH Cursor2 INTO @КодВагона, @ТипВагона, @НомерВагона
	END
	CLOSE Cursor2
	DEALLOCATE Cursor2
	FETCH Cursor1 INTO @КодРейса, @ДатаРейса
END
CLOSE Cursor1
DEALLOCATE Cursor1
SELECT * FROM #СведенияОВагонах
GO
/*
EXEC СписокВагоновНаМаршрут 112
GO
*/
DROP PROC СписокСтанцийМаршрута
GO

--параметр - номер поезда из таблицы "Маршрут"
--результат - список станций на выбранный маршрут
CREATE PROC СписокСтанцийМаршрута @НомерПоезда INT
AS
CREATE TABLE #СписокСтанций(Станция VARCHAR(50), ВремяОтправления TIME, ВремяПрибытия TIME, Номер INT, Тип INT)
DECLARE @КодМаршрута INT = (SELECT Код FROM Маршрут WHERE Маршрут.Номер_Поезда = @НомерПоезда)
DECLARE КурсорПоСтанциям CURSOR FOR
	SELECT Название, Время_Прибытия, Время_Отправления, Номер_На_Маршруте, Тип
	FROM Станции_На_Маршруте, Станция
	WHERE Станция.Код = Код_Станции AND Код_Маршрута = @КодМаршрута
OPEN КурсорПоСтанциям
DECLARE @Станция VARCHAR(50), @ВремяОтправления TIME, @ВремяПрибытия TIME, @Номер INT, @Тип INT
FETCH КурсорПоСтанциям INTO @Станция, @ВремяОтправления, @ВремяПрибытия, @Номер, @Тип
WHILE @@FETCH_STATUS = 0
BEGIN 
	INSERT INTO #СписокСтанций VALUES(@Станция, @ВремяОтправления, @ВремяПрибытия, @Номер, @Тип)
	FETCH КурсорПоСтанциям INTO @Станция, @ВремяОтправления, @ВремяПрибытия, @Номер, @Тип
END
CLOSE КурсорПоСтанциям
DEALLOCATE КурсорПоСтанциям
SELECT * FROM #СписокСтанций
GO
/*
EXEC СписокСтанцийМаршрута 704
GO
*/
DROP FUNCTION СредняяСтоимостьТипа
GO

--параметр - код типа вагона из таблицы "Типы_Вагонов"
--результат - выводит среднюю стоимость по типу вагона
CREATE FUNCTION СредняяСтоимостьТипа (@КодТипаВагонов INT)
RETURNS INT
BEGIN
	RETURN (SELECT AVG(Цена) FROM Билеты WHERE Код_Вагона IN 
		(SELECT Код FROM Вагоны WHERE Код_Типа_Вагона = @КодТипаВагонов))
END
GO
/*
SELECT Тип_Вагона, dbo.СредняяСтоимостьТипа(Код) as [Средняя Цена] FROM Типы_Вагонов
GO
*/

DROP FUNCTION СписокРаботниковРейса
GO

--результат - список работников, работающих на самом прибыльном маршруте
CREATE FUNCTION СписокРаботниковРейса ()
RETURNS
@Список TABLE(
	ФИО VARCHAR(50),
	Должность VARCHAR(50),
	Дата DATE,
	[Номер Поезда] INT
)
BEGIN
	DECLARE @ВременнаяТаблица TABLE (КодРейса INT, Сумма MONEY)
	INSERT INTO @ВременнаяТаблица 
	SELECT Код_Рейса, SUM(Цена) FROM Билеты, Вагоны WHERE Код_Вагона = Вагоны.Код
	GROUP BY Код_Рейса
	DECLARE @КодРейса INT = (SELECT КодРейса FROM @ВременнаяТаблица WHERE Сумма = (SELECT MAX(Сумма) FROM @ВременнаяТаблица))
	INSERT INTO @Список
	SELECT ФИО, Должность, Дата, Номер_Поезда
	FROM Рейс, Маршрут, Работники_В_Рейсе, Работники, Должность
	WHERE Рейс.Код = @КодРейса AND Маршрут.Код = Рейс.Код_Маршрута AND Работники_В_Рейсе.Код_Рейса = @КодРейса AND
		Работники_В_Рейсе.Код_Работника = Работники.Код AND Работники.Код_Должности = Должность.Код
	RETURN
END
GO

SELECT Код_Рейса, SUM(Цена) FROM Билеты, Вагоны WHERE Код_Вагона = Вагоны.Код
GROUP BY Код_Рейса
GO

SELECT * FROM Работники_В_Рейсе
GO

SELECT * FROM СписокРаботниковРейса()
GO

DROP PROC СамыйДешёвыйБилет
GO

--параметр - название типа вагона из таблицы "Типы_Вагонов"
--результат - самый дешёвый билет на определенный тип вагона
CREATE PROC СамыйДешёвыйБилет @ТипВагона VARCHAR(50)
AS
DECLARE @КодТипаВагона INT = (SELECT Код FROM Типы_Вагонов WHERE @ТипВагона = Тип_Вагона)
DECLARE @КодВагона INT = (SELECT MIN(Код) FROM Вагоны WHERE @КодТипаВагона = Код_Типа_Вагона)
DECLARE @КодБилета INT = (SELECT MIN(Код) FROM Билеты WHERE Код_Вагона = @КодВагона)
DECLARE @Цена MONEY = (SELECT TOP 1 Цена FROM Билеты WHERE Билеты.Код = @КодБилета)
DECLARE @Результат MONEY = @Цена
WHILE @КодВагона IS NOT NULL
BEGIN
	SET @КодБилета = (SELECT MIN(Код) FROM Билеты WHERE Код_Вагона = @КодВагона)
	WHILE @КодБилета IS NOT NULL
	BEGIN
		SET @Цена = (SELECT Цена FROM Билеты WHERE Билеты.Код = @КодБилета)
		IF (@Результат > @Цена)
		BEGIN
			SET @Результат = @Цена
		END
		SET @КодБилета = (SELECT MIN(Код) FROM Билеты WHERE Билеты.Код > @КодБилета AND Код_Вагона = @КодВагона)
	END		
	SET @КодВагона = (SELECT MIN(Код) FROM Вагоны WHERE Вагоны.Код > @КодВагона AND @КодТипаВагона = Код_Типа_Вагона)
END
RETURN @Результат
GO
/*
SELECT * FROM ПредставлениеБилетов
GO
*/
DECLARE @Результат MONEY = 0
EXEC @Результат = СамыйДешёвыйБилет 'Купе'
SELECT @Результат
GO

