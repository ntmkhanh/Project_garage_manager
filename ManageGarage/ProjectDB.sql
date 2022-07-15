use managegarage;

/* Drop Table */
drop table thanhtoan;
drop table nhanvien;
drop table dichvu;
drop table khachhang;

CREATE TABLE NHANVIEN(
    MaNVIEN VARCHAR(5) PRIMARY KEY,
    HOTEN VARCHAR(50) NOT NULL,
    NGAYSINH DATE,
    SODT CHAR(11)
);
alter table nhanvien
add constraint SODT_CK check(sodt REGEXP '^[0-9]{3}-[0-9]{7}$');
select * from nhanvien;

CREATE TABLE DICHVU(
    MaDVU VARCHAR(5) PRIMARY KEY,
    TENDV VARCHAR(100) NOT NULL,
    GIA INT
);
alter table dichvu 
add constraint GIA_CK check(GIA>=0);

CREATE TABLE KHACHHANG(
    MaKHANG VARCHAR(5) PRIMARY KEY,
    TENKH VARCHAR(50) NOT NULL,
    Bienso varchar(10) NOT NULL,
    SoDTKH VARCHAR(11) NOT NULL
);
alter table khachhang
add constraint BSO_CK check(BienSo REGEXP '^[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{5}$');

CREATE TABLE THANHTOAN(
    MaTTOAN VARCHAR(5) PRIMARY KEY,
    MaNVIEN VARCHAR(5),
    MaKHANG VARCHAR(5),
    MaDVU VARCHAR(5),
    foreign key (MaNVIEN) references NHANVIEN(MaNVIEN) ON DELETE SET NULL,
    foreign key (MaKHANG) references KHACHHANG(MaKHANG) ON DELETE SET NULL,
    foreign key (MaDVU) references DICHVU(MaDVU) ON DELETE SET NULL,
	MaHDon VARCHAR(5),
    NGAYTT DATE
);
drop table thanhtoan;
select * from thanhtoan;
insert into thanhtoan values('0123','00001','455','122','555','2021/01/07');
insert into thanhtoan values('0124','00002','455','123','555','2021/01/07');
insert into thanhtoan values('0125','00002','455','123','555','2021/01/07');
insert into thanhtoan values('0126','00002','455','123','555','2021/01/07');
select * from thanhtoan;

insert into NHANVIEN(MaNVIEN,HoTen,NgaySinh,SoDT) values('00001','Luu Tuan Dat','2001/01/18','077-3046705');
insert into NHANVIEN(MaNVIEN,HoTen,NgaySinh,SoDT) values('00002','Lao Quoc Nghia','2001/05/23','086-8944775');
insert into NHANVIEN(MaNVIEN,HoTen,NgaySinh,SoDT) values('00003','Bui Quang Duc','2001/10/21','085-5065055');
select * from nhanvien;

insert into DICHVU values('122','Bao tri xe SUV', 450000);
insert into DICHVU values('123','Bao tri xe Sedan', 350000);
insert into DICHVU values('124','Thay bugi xe', 100000);
insert into DICHVU values('125','Thay che hoa khi,chinh may', 350000);
select * from dichvu;

insert into KHACHHANG values('455','Nguyen Cao Hung','64B2-33033','056-8812099');
insert into KHACHHANG values('458','Nguyen Duy Khang','64B2-33033','057-4572648');
select * from khachhang;
                  
/*tim kiem NHANVIEN bang ten*/
delimiter //
CREATE PROCEDURE FINDTen_NHANVIEN(in pHOTEN varchar(50))
BEGIN
    SELECT * from NHANVIEN where pHOTEN = HOTEN;
    commit;
end //

/*tim kiem KHACHHANG bang ten*/
delimiter //
CREATE PROCEDURE FINDTen_KHANG(in pTenKH varchar(50))
BEGIN
    SELECT * from KHACHHANG where pTenKH = TenKH;
end //

/*tim kiem DICHVU bang ten*/
delimiter //
CREATE PROCEDURE FINDTen_DICHVU(in pTenDV varchar(50))
BEGIN
    SELECT * from DichVu where pTenDV = TenDV;
end //

call findten_Nhanvien("Luu Tuan Dat");

/*tim kiem NHANVIEN bang id*/
delimiter //
CREATE PROCEDURE FINDID_NHANVIEN(in pMaNVIEN varchar(5))
BEGIN
    SELECT * from NHANVIEN where pMaNVIEN = MaNVIEN;
    commit;
end //

/* Update NHANVIEN */
delimiter //
CREATE PROCEDURE UPDATE_NVIEN(in pmanvien char(5),in photen varchar(50),
						 in pngaySinh date,in psodt char(11))
BEGIN
    UPDATE NHANVIEN SET HoTen=photen,NgaySinh=pngaySinh,SoDT=psodt where MaNVien=pmanvien;
    commit;
end //

select * from nhanvien;
call UPDATE_NVIEN('00004','Luu Tuan','2001/01/18','077-3046705');

/* them NHANVIEN */
delimiter //
create procedure THEM_NV(in pmanvien char(5),in photen varchar(50),
						 in pngaySinh date,in psodt char(11))
begin
	insert into nhanvien values (pmanvien, photen, pngaySinh, psodt);
end; //

/* them KHACHHANG */
delimiter //
create procedure THEM_KH(in pmakhang char(5),in ptenkh varchar(50),
						 in pbienso varchar(10),in psodt char(11))
begin
	insert into khachhang values (pmakhang, ptenkh, pbienso, psodt);
end; //

/* sua KHACHHANG */
delimiter //
CREATE PROCEDURE UPDATE_KHANG(in pmakhang char(5),in ptenkh varchar(50),
						 in pbienso varchar(10),in psodt char(11))
BEGIN
    UPDATE KHACHHANG SET TenKH=ptenkh,BienSo=pbienso,SoDTKH=psodt where MaKHANG=pmakhang;
    commit;
end //

/* xoa KHACHHANG */
delimiter //
CREATE PROCEDURE DEL_KHANG(in pmakhang char(5))
BEGIN
	DELETE FROM KHACHHANG WHERE MaKHANG=pmakhang;
    commit;
END //

/* xoa NHANVIEN */
delimiter //
CREATE PROCEDURE DEL_NVIEN(in pmanv char(5))
BEGIN
	DELETE FROM NHANVIEN WHERE MaNVien=pmanv;
    commit;
END //

/* them DICHVU*/
delimiter //
CREATE PROCEDURE THEM_DV(
	in pMaDVU varchar(5), 
    in pTENDV varchar(100),
    in pGIA int
)
BEGIN
    insert into DICHVU values(pMaDVU, pTENDV, pGIA);
    commit;
end //
call them_dvu('129','Rua Xe',40000);
select * from dichvu;

/* xoa DICHVU */
delimiter //
CREATE PROCEDURE DEL_DICHVU(in pMaDVU varchar(5))
BEGIN
    DELETE FROM DICHVU WHERE MaDVU = pMaDVU;
    commit;
end //
drop procedure del_dichvu;

/* chinh gia DICHVU*/
delimiter //
CREATE PROCEDURE UPDATE_DICHVU(in pMaDVU varchar(5), in pGIA int)
BEGIN
    update DICHVU set gia = pgia where MaDVU = pMaDVU;
    commit;
end //

/* them THANHTOAN */
delimiter //
CREATE PROCEDURE THEM_TT(in pMaTTOAN varchar(5), in pMaNVien varchar(5), in pMaKHANG varchar(5), 
							in pMaDVu varchar(5), in pMaHDon varchar(5), in pNgayTT date)
BEGIN
    insert into THANHTOAN values(pMaTTOAN,pMaNVien,pMaKHANG,pMaDVU,pMaHDON,pNgayTT);
    commit;
end //

/* xoa THANHTOAN */
delimiter //
CREATE PROCEDURE DEL_TTOAN(in pMaTT varchar(5))
BEGIN
    DELETE FROM THANHTOAN WHERE MaTToan = pMaTT;
    commit;
end //

/* Tinh tong hoa don */
delimiter //
CREATE PROCEDURE SUM_BILL(in pmahdon varchar(5))
BEGIN
	select sum(dv.gia) as GiaDV
	from thanhtoan as tt join dichvu as dv on tt.madvu=dv.madvu
	where pmahdon=tt.mahdon
    group by pmahdon=tt.mahdon;
	commit;
END //
drop procedure sum_bill;
call sum_bill('666');
select * from thanhtoan;

/* Lich su khach hang */ 
delimiter //
CREATE PROCEDURE HISTORY_CUSTOMER(in pTenKH varchar(50))
BEGIN
	select tt.mahdon as mhd, dv.tendv as dv, dv.gia as g, tt.ngaytt as ngay
	from khachhang as kh join thanhtoan as tt on kh.makhang=tt.makhang
			join dichvu as dv on tt.madvu=dv.madvu 
	where kh.tenkh=pTenKH;
END //
drop procedure history_customer;
call history_customer('Nguyen Duy Khang');

/* Chi tiet theo ma hoa don */
delimiter //
CREATE PROCEDURE VIEW_BILL(in pmahdon varchar(5))
BEGIN
	select tt.mahdon as mahdon,tt.mattoan as mattoan,nv.hoten as tennvien,kh.tenkh as tenkh,dv.tendv as tendv,dv.gia as gia
	from thanhtoan as tt join nhanvien as nv on tt.manvien=nv.manvien join khachhang as kh on tt.makhang=kh.makhang
					join dichvu as dv on tt.madvu=dv.madvu
	where mahdon=pmahdon;
END //
drop procedure view_bill;
call view_bill('555');

/* Thong ke hoa don theo ngay */
delimiter //
CREATE PROCEDURE THONGKE(in pngay date)
BEGIN
	select tt.ngaytt as ngay,tt.mahdon as mahdon,tt.mattoan as mattoan,nv.hoten as tennvien,kh.tenkh as tenkh,dv.tendv as tendv,dv.gia as gia
	from thanhtoan as tt join nhanvien as nv on tt.manvien=nv.manvien join khachhang as kh on tt.makhang=kh.makhang
					join dichvu as dv on tt.madvu=dv.madvu
	where ngaytt=pngay;
END //

/* Tong thong ke hoa don theo ngay */
delimiter //
CREATE PROCEDURE SUM_THONGKE(in pngay date)
BEGIN
	select sum(dv.gia) as thongke
	from thanhtoan as tt join dichvu as dv on tt.madvu=dv.madvu
	where ngaytt=pngay
    group by ngaytt=pngay;
	commit;
END //
call sum_thongke('2021-12-01');
call thongke('2021-12-01');

/* Ham Giam gia hoa don */
delimiter //
create function SALE_BILL(pmahdon varchar(5))
returns int
begin
	declare sale_b int;
    set sale_b = 0;
    select sum(dv.gia) into sale_b
	from thanhtoan as tt join dichvu as dv on tt.madvu=dv.madvu
    where pmahdon=tt.mahdon
    group by pmahdon=tt.mahdon;
    if sale_b >= 3000000 then
		set sale_b=sale_b - ((sale_b * 10)/100);
	elseif sale_b >= 1000000 then
		set sale_b=sale_b - ((sale_b * 5)/100); 
	else
		set sale_b=sale_b;
	end if;
    return sale_b;
end //


