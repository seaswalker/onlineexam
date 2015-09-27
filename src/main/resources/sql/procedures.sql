create procedure getExamById(in id int)
begin
	declare examid int;
	declare expire datetime;
	declare loopFlag boolean default false;
	declare idCursor cursor for select e.id, e.endtime from exam e where e.id = id and status = 'RUNNING';
	declare continue handler for NOT FOUND set loopFlag = true;
	open idCursor;
	idCursorLoop:loop
		fetch idCursor into examid, expire;
		if now() > expire then
			update exam e set e.status = 'RUNNED' where e.id = examid;
		end if;
		if loopFlag then
			leave idCursorLoop;
		end if;
	end loop;
	select * from exam e where e.id = id;
end//

create procedure getExamByTeacher(in pn int, in ps int, in tid varchar(20))
begin
	declare start int;
	declare examid int;
	declare expire datetime;
	declare loopFlag boolean default false;
	declare pageCursor cursor for select e.id, e.endtime from exam e where e.tid = tid and status = 'RUNNING' limit start, ps;
	declare continue handler for NOT FOUND set loopFlag = true;
	set start = (pn - 1) * ps;
	open pageCursor;
	set loopFlag = false;
	pageCursorLoop:loop
		fetch pageCursor into examid, expire;
		if now() > expire then
			update exam e set e.status = 'RUNNED' where e.id = examid;
		end if;
		if loopFlag then
			leave pageCursorLoop;
		end if;
	end loop;
	select * from exam e where e.tid = tid limit start, ps;
end//

create procedure getExamByStudent(in sid varchar(20), in pn int, in ps int)
begin
	declare start int;
	declare examid int;
	declare expire datetime;
	declare loopFlag boolean default false;
	declare pageCursor cursor for select e.id, e.endtime from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = sid)) and status = 'RUNNING' limit start, ps;
	declare continue handler for NOT FOUND set loopFlag = true;
	set start = (pn - 1) * ps;
	open pageCursor;
	pageCursorLoop:loop
		fetch pageCursor into examid, expire;
		if now() > expire then
			update exam e set e.status = 'RUNNED' where e.id = examid;
		end if;
		if loopFlag then
			leave pageCursorLoop;
		end if;
	end loop;
	select e.* from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = sid)) limit start, ps;
end//