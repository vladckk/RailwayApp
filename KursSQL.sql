DROP VIEW �������������������
GO

CREATE VIEW ������������������� AS
SELECT ����.���, ����, �����_������[����� ������]
FROM ����, �������
WHERE �������.��� = ����.���_��������
GO
/*
SELECT * FROM �������������������
GO
*/
DROP VIEW �����������������������
GO

DROP VIEW ��������������������
GO

CREATE VIEW �������������������� AS
SELECT ������.���, ����, [����� ������], [����� ������], �����_�����, [��� ������], ����, ���, ������.�������� [������� �����������], ����.�������� [������� ��������]
FROM ��������������������, ������� ������, ������� ����, ������
WHERE ���_�������_����������� = ������.��� AND ���_�������_�������� = ����.��� AND ��������������������.��� = ���_������
GO
/*
SELECT * FROM ��������������������
GO
*/
CREATE VIEW ����������������������� AS
SELECT ���������.���, ���, ���������
FROM ���������, ���������
WHERE ���������.��� = ���������.���_���������
GO

DROP VIEW ������������������������������
GO

CREATE VIEW ������������������������������ AS
SELECT �������_��_��������.���, �����_������ [����� ������], ��������[�������], �����_��������[����� ��������], �����_�����������[����� �����������], �����_��_��������[����� �� ��������], ���
FROM �������, �������, �������_��_��������
WHERE �������.��� = ���_�������� AND �������.��� = ���_�������
GO
/*
SELECT * FROM ������������������������������
GO
*/
DROP VIEW �����������������������������
GO

CREATE VIEW ����������������������������� AS
SELECT ���������_�_�����.���, ���, ���������, ����, [����� ������]
FROM ���������_�_�����, �����������������������, �������������������
WHERE ���_��������� = �����������������������.��� AND ���_����� = �������������������.��� 
GO

SELECT * FROM �����������������������������
GO

/*
SELECT * FROM �����������������������
GO
*/

/*
SELECT * FROM ���������_�_�����, ������������������� WHERE ���� > '2020-05-06' AND
	���_��������� = 2 AND �������������������.��� = ���_�����
GO
*/
DROP TRIGGER trg_���������������
GO

--������� ����������� ��������� �������� ������� �� ������� "�������"
CREATE TRIGGER trg_��������������� ON �������
	AFTER DELETE
AS
DELETE FROM �������_��_��������
WHERE ���_������� IN (SELECT ��� FROM deleted)
DELETE FROM ������
WHERE ���_�������_����������� IN (SELECT ��� FROM deleted) OR ���_�������_�������� IN (SELECT ��� FROM deleted)
GO

DROP TRIGGER trg_�������������������
GO

--������� ����������� ��������� �������� ������� �� ������� "����_�������"
CREATE TRIGGER trg_������������������� ON ����_�������
	AFTER DELETE
AS
DELETE FROM ������
WHERE ���_������ IN (SELECT ������.��� FROM ������, deleted WHERE ���_����_������ = deleted.���)
DELETE FROM ������
WHERE ���_����_������ IN (SELECT ��� FROM deleted)
GO

DROP TRIGGER trg_������ 
GO

--������� ������ �������� �� ������������� ������� �� �����, ���������� ��������
CREATE TRIGGER trg_������ ON ������
	AFTER DELETE
AS
IF EXISTS(SELECT * FROM ������ WHERE ���_������ IN (SELECT ��� FROM deleted))
BEGIN
	RAISERROR('������ ������� �����, ������ ��� �� ���� ���� ������!', 16, 2)
	ROLLBACK
END
GO

DROP TRIGGER trg_����_����������
GO

CREATE TRIGGER trg_����_���������� ON ����
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ������� WHERE �������.��� = ���_��������)
BEGIN
	RAISERROR('������ �������� ���� �� �������������� �������!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_������_����������_��������������
GO

CREATE TRIGGER trg_������_����������_�������������� ON ������
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ������ WHERE ������.��� = ���_������)
BEGIN
	RAISERROR('������ �������� ����� �� �������������� �����!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_������_����������_��������������
GO

CREATE TRIGGER trg_������_����������_�������������� ON ������
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ������� WHERE �������.��� = ���_�������_�����������)
BEGIN
	RAISERROR('������ �������� ����� �� �������������� ������� �����������!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_������_����������_������������
GO

CREATE TRIGGER trg_������_����������_������������ ON ������
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ������� WHERE �������.��� = ���_�������_��������)
BEGIN
	RAISERROR('������ �������� ����� �� �������������� ������� ��������!', 16, 1)
	ROLLBACK
END
GO

DROP TRIGGER trg_������_���������
GO

CREATE TRIGGER trg_������_��������� ON ������
	AFTER UPDATE
AS
IF UPDATE(���_����_������)
	--IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ����_������� WHERE ����_�������.��� = ���_����_������)
	IF ((SELECT TOP 1 inserted.���_����_������ FROM inserted, deleted WHERE inserted.��� = deleted.���
		AND inserted.���_����_������ <> deleted.���_����_������) NOT IN (SELECT ��� FROM ����_�������))
	BEGIN
		RAISERROR('������ �������� ����� �� ����� ��������������� ����!', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_�����������������_���������������� 
GO

CREATE TRIGGER trg_�����������������_���������������� ON �������_��_��������
	AFTER UPDATE
AS
IF UPDATE(���_�������)
	IF ((SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted, ������� WHERE �������.��� = ���_�������)) 
	BEGIN
		RAISERROR('������ �������� ������� �� �������� �� �������������� �������!', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_����_�������������
GO

--������� ����������� ���� �� �����������(������ ������� ����� �� 1991 ����) ����� �������������� �������
CREATE TRIGGER trg_����_������������� ON ����
	AFTER UPDATE
AS
IF UPDATE(����)
	IF ((SELECT COUNT(*) FROM inserted WHERE ���� > '1991-01-01') <> (SELECT COUNT(*) FROM deleted))
	BEGIN
		RAISERROR('� ���� ������ ������ ������� ������ ������, ������� ���� ������ 1991 ����', 16, 1)
		ROLLBACK
	END
GO

DROP TRIGGER trg_����_��������������
GO

--������� ����������� ����� ������ � ������� "����" �� �����������(������ ������� ����� �� 1991 ����) 
CREATE TRIGGER trg_����_�������������� ON ����
	AFTER INSERT
AS
IF (SELECT COUNT(*) FROM inserted) <> (SELECT COUNT(*) FROM inserted WHERE ���� > '1991-01-01')
BEGIN
	RAISERROR('������ �������� ���� ������ 1991 ����', 16, 1)
	ROLLBACK
END
GO
--����������� ����� ������������� ���� ������
ALTER TABLE ������� 
	ADD ����������������� INT NOT NULL DEFAULT 0
GO

UPDATE ������� SET ����������������� = (SELECT COUNT(*) FROM �������_��_�������� WHERE �������_��_��������.���_�������� = �������.���)
GO
/*
SELECT * FROM �������
GO
*/
DROP TRIGGER ������������������
GO

--������� ��� ���������� ��������� ���� "�����������������" ������� "�������"
CREATE TRIGGER ������������������ ON �������_��_��������
	AFTER INSERT, DELETE, UPDATE
AS
UPDATE ������� SET ����������������� += ���
FROM (SELECT ���_��������, COUNT(*) ��� FROM inserted GROUP BY ���_��������) ins
WHERE �������.��� = ���_��������
UPDATE ������� SET ����������������� -= ���	
FROM (SELECT ���_��������, COUNT(*) ��� FROM deleted GROUP BY ���_��������) del
WHERE �������.��� = ���_��������
GO

DROP VIEW ��������������������
GO

CREATE VIEW �������������������� AS
SELECT ������.���, ����, �����_������[����� ������], ���_������[��� ������], �����[����� ������]
FROM ������, ����, �������, ����_�������
WHERE ���_����� = ����.��� AND �������.��� = ���_�������� AND ����_�������.��� = ���_����_������
GO

DROP TRIGGER ����������������
GO

--������� ��� ���������� ������� � ������� "������" ����� �������������
CREATE TRIGGER ���������������� ON ��������������������
	INSTEAD OF INSERT
AS
INSERT INTO ������(���_�����, ���_����_������, �����)
	SELECT ����.���, ����_�������.���, [����� ������]
	FROM inserted INNER JOIN ���� ON ����.���� = inserted.����
	INNER JOIN ������� ON �������.�����_������ = inserted.[����� ������] AND �������.��� = ����.���_��������
	INNER JOIN ����_������� ON ����_�������.���_������ = inserted.[��� ������]
	WHERE inserted.���� IS NOT NULL AND inserted.[����� ������] IS NOT NULL AND inserted.[����� ������] IS NOT NULL
		AND inserted.[��� ������] IS NOT NULL
PRINT('Adding information')
GO

/*
INSERT INTO �������������������� (����, [����� ������], [��� ������], [����� ������]) VALUES ('2018-10-05', 704, '��������', 33)
GO
*/
DROP TRIGGER ������������
GO

--������� ��� �������������� ������� ������� "������" ����� �������������
CREATE TRIGGER ������������ ON ��������������������
	INSTEAD OF UPDATE
AS
IF NOT UPDATE (����) AND NOT UPDATE ([����� ������])
	UPDATE ������ SET ���_����_������ = ����_�������.���, ����� = inserted.[����� ������]
	FROM ������ ��� INNER JOIN inserted ON inserted.��� = ���.���
		INNER JOIN ����_������� ON ����_�������.���_������ = inserted.[��� ������]
	WHERE inserted.[����� ������] IS NOT NULL AND inserted.[��� ������] IS NOT NULL
ELSE IF NOT UPDATE ([��� ������])
	UPDATE ������ SET ���_����� = ����.���, ����� = inserted.[����� ������]
	FROM ������ ��� INNER JOIN inserted ON inserted.��� = ���.���
		INNER JOIN ���� ON ����.���� = inserted.����
		INNER JOIN ������� ON �������.�����_������ = inserted.[����� ������] AND �������.��� = ����.���_��������
	WHERE inserted.���� IS NOT NULL AND inserted.[����� ������] IS NOT NULL AND inserted.[����� ������] IS NOT NULL
ELSE
	UPDATE ������ SET ���_����� = ����.���, ���_����_������ = ����_�������.���, ����� = inserted.[����� ������]
	FROM ������ ��� INNER JOIN inserted ON inserted.��� = ���.���
		INNER JOIN ���� ON ����.���� = inserted.����
		INNER JOIN ������� ON �������.�����_������ = inserted.[����� ������] AND �������.��� = ����.���_��������
		INNER JOIN ����_������� ON ����_�������.���_������ = inserted.[��� ������]
	WHERE inserted.���� IS NOT NULL AND inserted.[����� ������] IS NOT NULL AND inserted.[����� ������] IS NOT NULL
		AND inserted.[��� ������] IS NOT NULL
	PRINT('Updating')
GO
/*
UPDATE �������������������� SET ���� = '2018-10-05', [����� ������] = 704, [��� ������] = '��������', [����� ������] = 111
WHERE ��� = 2
GO
*/
DROP TRIGGER ��������������
GO

--������� ��� �������� ������� ����� �������������
CREATE TRIGGER �������������� ON ��������������������
	INSTEAD OF DELETE
AS
	DELETE FROM	������ WHERE ��� IN (SELECT ��� FROM deleted)
GO
/*
DELETE FROM ��������������������
WHERE ��� = 8
GO
*/

/*
CREATE PROC ���������� @��������� INT, @��������������������� INT, @������������������ INT
AS
	
GO
*/

DROP FUNCTION ������������
GO

--�������� - ��� ���������, ������� ���������� �� ��� ��������� � ������� ������������ ����.
--��������� - ������ ������ � �������������� ����������� �� ����������� ������� �� �����
CREATE FUNCTION ������������ (@������������ INT)
RETURNS TABLE
AS
RETURN
	SELECT ����, [����� ������], �����[����� ������], ���_������[��� ������]
	FROM ���������_�_�����, �������������������, ������, ����_�������
	WHERE @������������ = ���_��������� AND ���������_�_�����.���_����� = �������������������.��� AND 
		�������������������.��� = ������.���_����� AND ���_����_������ = ����_�������.���
GO

/*
SELECT * FROM ���������_�_�����

SELECT * FROM ������������(2)
GO
*/
DROP PROC ����������������
GO


CREATE PROC ���������������� @������������ INT
AS
DECLARE @���������� INT = (SELECT COUNT(*) FROM ������������(@������������))
RETURN @����������
GO

DROP PROC ��������������������
GO

--�������� - ��� ���������, ������� ���������� �� ��� ��������� � ������� ������������ ����.
--��������� - ���������� ����������� ���������� �� �����, �� ������� ��� ��������� ��������
CREATE PROC �������������������� @������������ INT
AS
DECLARE @���������� INT = (SELECT COUNT(*) FROM ���������_�_�����, ����, ������, ������
	WHERE @������������ = ���_��������� AND ���������_�_�����.���_����� = ����.��� AND ����.��� = ������.���_�����
		AND ������.��� = ���_������)
RETURN @����������
GO

DROP PROC �����������
GO
/*
SELECT * FROM ��������������������
GO
*/

--�������� @��������� - �������� ���� ������ �� ������� "����_�������"
--�������� @������� - �����������, �� ������� ���������� ���� �������
--��������� - ��������� ��� �� ������ � ������������ ����� ������ � �� ��������� �����������
CREATE PROC ����������� @��������� VARCHAR(50), @������� FLOAT
AS
UPDATE ������ SET ���� = ���� * @�������
WHERE ���_������ IN (SELECT ��� FROM ������ WHERE ���_����_������ = 
	(SELECT ����_�������.��� FROM ����_������� WHERE ���_������ = @���������))
GO
/*
EXEC ����������� '��������', 1.2
GO
*/
DROP PROC �������������
GO

--�������� - ��������� ����
--��������� - ������ ������� ������� � ������������ ����
CREATE PROC ������������� @���� DATE
AS
SELECT ����, �����_������ [����� ������], �����_����� [����� �����], ����� [����� ������], ���_������[��� ������], ����, ���,
	������.�������� [������� �����������], ����.�������� [������� ��������]
INTO #������
FROM ������ INNER JOIN ������ ON ������.��� = ���_������
	INNER JOIN ���� ON ���_����� = ����.���
	INNER JOIN ������� ON ���_�������� = �������.���
	INNER JOIN ����_������� ON ����_�������.��� = ���_����_������
	INNER JOIN ������� AS ������ ON ������.��� = ���_�������_�����������
	INNER JOIN ������� AS ���� ON ����.��� = ���_�������_�������� 
WHERE ���� > @����
SELECT * FROM #������
GO

EXEC ������������� '2018-10-04'
GO

DROP PROC ���������������������� 
GO
/*
SELECT * FROM ������
GO
*/
--�������� - ����� ������ �� ������� �������
--��������� - ���������� � ������� � ����� ��������� ������� �� ������ ����� �� ��������� �������
CREATE PROC ���������������������� @����������� INT
AS
CREATE TABLE #���������������� (��������� DATE, ����������� INT, ��������� VARCHAR(50), ����� INT)
DECLARE @����������� INT = (SELECT ��� FROM ������� WHERE �����_������ = @�����������)
DECLARE Cursor1 CURSOR FOR
	SELECT ���, ���� FROM ���� WHERE @����������� = ���_��������
OPEN Cursor1
DECLARE @�������� INT, @��������� DATE
FETCH Cursor1 INTO @��������, @���������
WHILE (@@FETCH_STATUS = 0)
BEGIN
	DECLARE Cursor2 CURSOR FOR
	SELECT ������.���, ���_������, ����� FROM ������, ����_������� WHERE ���_����� = @�������� AND ���_����_������ = ����_�������.���
	OPEN Cursor2
	DECLARE @��������� INT, @��������� VARCHAR(50), @����������� INT
	FETCH Cursor2 INTO @���������, @���������, @�����������
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		DECLARE @����� INT = (SELECT ISNULL(SUM(����), 0) FROM ������ WHERE ���_������ = @���������)
		INSERT INTO #���������������� VALUES (@���������, @�����������, @���������, @�����)
		FETCH Cursor2 INTO @���������, @���������, @�����������
	END
	CLOSE Cursor2
	DEALLOCATE Cursor2
	FETCH Cursor1 INTO @��������, @���������
END
CLOSE Cursor1
DEALLOCATE Cursor1
SELECT * FROM #����������������
GO
/*
EXEC ���������������������� 112
GO
*/
DROP PROC ���������������������
GO

--�������� - ����� ������ �� ������� "�������"
--��������� - ������ ������� �� ��������� �������
CREATE PROC ��������������������� @����������� INT
AS
CREATE TABLE #�������������(������� VARCHAR(50), ���������������� TIME, ������������� TIME, ����� INT, ��� INT)
DECLARE @����������� INT = (SELECT ��� FROM ������� WHERE �������.�����_������ = @�����������)
DECLARE ���������������� CURSOR FOR
	SELECT ��������, �����_��������, �����_�����������, �����_��_��������, ���
	FROM �������_��_��������, �������
	WHERE �������.��� = ���_������� AND ���_�������� = @�����������
OPEN ����������������
DECLARE @������� VARCHAR(50), @���������������� TIME, @������������� TIME, @����� INT, @��� INT
FETCH ���������������� INTO @�������, @����������������, @�������������, @�����, @���
WHILE @@FETCH_STATUS = 0
BEGIN 
	INSERT INTO #������������� VALUES(@�������, @����������������, @�������������, @�����, @���)
	FETCH ���������������� INTO @�������, @����������������, @�������������, @�����, @���
END
CLOSE ����������������
DEALLOCATE ����������������
SELECT * FROM #�������������
GO
/*
EXEC ��������������������� 704
GO
*/
DROP FUNCTION ��������������������
GO

--�������� - ��� ���� ������ �� ������� "����_�������"
--��������� - ������� ������� ��������� �� ���� ������
CREATE FUNCTION �������������������� (@�������������� INT)
RETURNS INT
BEGIN
	RETURN (SELECT AVG(����) FROM ������ WHERE ���_������ IN 
		(SELECT ��� FROM ������ WHERE ���_����_������ = @��������������))
END
GO
/*
SELECT ���_������, dbo.��������������������(���) as [������� ����] FROM ����_�������
GO
*/

DROP FUNCTION ���������������������
GO

--��������� - ������ ����������, ���������� �� ����� ���������� ��������
CREATE FUNCTION ��������������������� ()
RETURNS
@������ TABLE(
	��� VARCHAR(50),
	��������� VARCHAR(50),
	���� DATE,
	[����� ������] INT
)
BEGIN
	DECLARE @���������������� TABLE (�������� INT, ����� MONEY)
	INSERT INTO @���������������� 
	SELECT ���_�����, SUM(����) FROM ������, ������ WHERE ���_������ = ������.���
	GROUP BY ���_�����
	DECLARE @�������� INT = (SELECT �������� FROM @���������������� WHERE ����� = (SELECT MAX(�����) FROM @����������������))
	INSERT INTO @������
	SELECT ���, ���������, ����, �����_������
	FROM ����, �������, ���������_�_�����, ���������, ���������
	WHERE ����.��� = @�������� AND �������.��� = ����.���_�������� AND ���������_�_�����.���_����� = @�������� AND
		���������_�_�����.���_��������� = ���������.��� AND ���������.���_��������� = ���������.���
	RETURN
END
GO

SELECT ���_�����, SUM(����) FROM ������, ������ WHERE ���_������ = ������.���
GROUP BY ���_�����
GO

SELECT * FROM ���������_�_�����
GO

SELECT * FROM ���������������������()
GO

DROP PROC �����������������
GO

--�������� - �������� ���� ������ �� ������� "����_�������"
--��������� - ����� ������� ����� �� ������������ ��� ������
CREATE PROC ����������������� @��������� VARCHAR(50)
AS
DECLARE @������������� INT = (SELECT ��� FROM ����_������� WHERE @��������� = ���_������)
DECLARE @��������� INT = (SELECT MIN(���) FROM ������ WHERE @������������� = ���_����_������)
DECLARE @��������� INT = (SELECT MIN(���) FROM ������ WHERE ���_������ = @���������)
DECLARE @���� MONEY = (SELECT TOP 1 ���� FROM ������ WHERE ������.��� = @���������)
DECLARE @��������� MONEY = @����
WHILE @��������� IS NOT NULL
BEGIN
	SET @��������� = (SELECT MIN(���) FROM ������ WHERE ���_������ = @���������)
	WHILE @��������� IS NOT NULL
	BEGIN
		SET @���� = (SELECT ���� FROM ������ WHERE ������.��� = @���������)
		IF (@��������� > @����)
		BEGIN
			SET @��������� = @����
		END
		SET @��������� = (SELECT MIN(���) FROM ������ WHERE ������.��� > @��������� AND ���_������ = @���������)
	END		
	SET @��������� = (SELECT MIN(���) FROM ������ WHERE ������.��� > @��������� AND @������������� = ���_����_������)
END
RETURN @���������
GO
/*
SELECT * FROM ��������������������
GO
*/
DECLARE @��������� MONEY = 0
EXEC @��������� = ����������������� '����'
SELECT @���������
GO

