/*==============================================================*/
/* Table: BILL_NO_CREATE                                        */
/*==============================================================*/
create table BILL_NO_CREATE 
(
   BILL_NO_NAME         VARCHAR2(255),
   BILL_QZ              VARCHAR2(255),
   CUR_MONTH            VARCHAR2(255),
   CUR_BILL_INDEX       VARCHAR2(255),
   MEMO                 VARCHAR2(255)
);

/*==============================================================*/
/* Table: fcs_alarm_handle_info                                 */
/*==============================================================*/
create table fcs_alarm_handle_info 
(
   id                   CHAR(36)             not null,
   alarm_id             CHAR(36),
   user_id              CHAR(36),
   handle_info          VARCHAR2(4000),
   handle_time          DATE,
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_ALARM_HANDLE_INFO primary key (id)
);

/*==============================================================*/
/* Table: fcs_alarm_info                                        */
/*==============================================================*/
create table fcs_alarm_info 
(
   id                   CHAR(36)             not null,
   alarm_type           VARCHAR2(255),
   alarm_level          VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   location_type        VARCHAR2(255),
   device_no            VARCHAR2(255),
   location             VARCHAR2(255),
   alarm_group          VARCHAR2(255),
   title                VARCHAR2(255),
   content              VARCHAR2(4000),
   handled              INTEGER              default 0,
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_ALARM_INFO primary key (id)
);

/*==============================================================*/
/* Table: fcs_alarm_read_stamp                                  */
/*==============================================================*/
create table fcs_alarm_read_stamp 
(
   id                   CHAR(36)             not null,
   user_id              CHAR(36),
   last_refresh_time    DATE,
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_ALARM_READ_STAMP primary key (id)
);

/*==============================================================*/
/* Table: fcs_battery_info                                      */
/*==============================================================*/
create table fcs_battery_info 
(
   id                   CHAR(36),
   sku_id               CHAR(36),
   battery_barcode      VARCHAR2(255),
   battery_status       VARCHAR2(255),
   remark               VARCHAR2(4000),
   test_complete_time   DATE,
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255)
);

/*==============================================================*/
/* Table: fcs_equipment                                         */
/*==============================================================*/
create table fcs_equipment 
(
   id                   CHAR(36)             not null,
   equip_name           VARCHAR2(255),
   equip_no             VARCHAR2(255),
   resource_no          VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   location_no          VARCHAR2(255),
   pos_x                INTEGER              default 0,
   pos_y                INTEGER              default 0,
   pos_z                INTEGER              default 0,
   equip_img            VARCHAR2(2000),
   equip_type           VARCHAR2(255),
   equip_model          VARCHAR2(255),
   equip_desc           VARCHAR2(2000),
   equip_vender         VARCHAR2(255),
   vender_phone         VARCHAR2(255),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_EQUIPMENT primary key (id)
);

/*==============================================================*/
/* Table: fcs_equipment_extension                               */
/*==============================================================*/
create table fcs_equipment_extension 
(
   id                   CHAR(36)             not null,
   equip_id             CHAR(36),
   ext_key              VARCHAR2(255),
   ext_value            VARCHAR2(255),
   ext_desc             VARCHAR2(2000),
   constraint PK_FCS_EQUIPMENT_EXTENSION primary key (id)
);

/*==============================================================*/
/* Table: fcs_fm_battery_in                                     */
/*==============================================================*/
create table fcs_fm_battery_in 
(
   id                   CHAR(36)             not null,
   form_no              VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   equip_no             VARCHAR2(255),
   sku_id               CHAR(36),
   battery_barcode      VARCHAR2(255),
   line_pos             VARCHAR2(255),
   line_channel_no      VARCHAR2(255),
   operate_datetime     DATE,
   create_mode          VARCHAR2(255),
   fm_status            VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_msg            VARCHAR2(4000),
   remark               VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_FM_BATTERY_IN primary key (id)
);

/*==============================================================*/
/* Table: fcs_fm_storage_out                                    */
/*==============================================================*/
create table fcs_fm_storage_out 
(
   id                   CHAR(36)             not null,
   form_no              VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   equip_no             VARCHAR2(255),
   sku_id               CHAR(36),
   battery_barcode      VARCHAR2(255),
   line_pos             VARCHAR2(255),
   line_channel_no      VARCHAR2(255),
   operate_datetime     DATE,
   create_mode          VARCHAR2(255),
   fm_status            VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_msg            VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   remark               VARCHAR2(4000),
   constraint PK_FCS_FM_STORAGE_OUT primary key (id)
);

/*==============================================================*/
/* Table: fcs_instruction                                       */
/*==============================================================*/
create table fcs_instruction 
(
   id                   CHAR(36)             not null,
   instr_no             VARCHAR2(255),
   instr_type           VARCHAR2(255),
   house_no             VARCHAR2(255),
   queue_no             VARCHAR2(255),
   scheduler_time       DATE,
   equip_no             VARCHAR2(255),
   pallet_no            VARCHAR2(255),
   form_no              VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   send_type            INTEGER              default 0,
   send_time            DATE,
   from_pos             VARCHAR2(255),
   to_pos               VARCHAR2(255),
   instr_level          INTEGER              default 0,
   move_policy          VARCHAR2(255),
   instr_status         VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_desc           VARCHAR2(4000),
   remark               VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_INSTRUCTION primary key (id)
);


/*==============================================================*/
/* Table: fcs_pallet_dispatch                                   */
/*==============================================================*/
create table fcs_pallet_dispatch 
(
   id                   CHAR(36)             not null,
   house_id             CHAR(36),
   container_id         CHAR(36),
   container_no         VARCHAR2(255),
   current_form_no      VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   pallet_status        VARCHAR2(255),
   split_policy         VARCHAR2(255),
   channel_policy       VARCHAR2(255),
   is_empty             INTEGER              default 0,
   enter_time           DATE,
   palletize_complete_time DATE,
   current_procedure_time DATE,
   pallet_split_time    DATE,
   dispatch_status      VARCHAR2(255),
   pos_type             VARCHAR2(255),
   current_pos          VARCHAR2(255),
   procedure_route      VARCHAR2(255),
   loc_change_times     INTEGER              default 0,
   error_code           VARCHAR2(255),
   error_desc           VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_PALLET_DISPATCH primary key (id)
);

/*==============================================================*/
/* Table: fcs_pallet_inner_detail                               */
/*==============================================================*/
create table fcs_pallet_inner_detail 
(
   id                   CHAR(36)             not null,
   pallet_dispatch_id   CHAR(36),
   sku_id               CHAR(36),
   channel_no           VARCHAR2(255),
   form_no              VARCHAR2(255),
   battery_barcode      VARCHAR2(255),
   battery_status       VARCHAR2(255),
   pallet_no            VARCHAR2(255),
   from_equip_no        VARCHAR2(255),
   from_clamp_no        VARCHAR2(255),
   from_pos_type        VARCHAR2(255),
   from_pos_no          VARCHAR2(255),
   from_pos_channel_no  VARCHAR2(255),
   to_equip_no          VARCHAR2(255),
   to_clamp_no          VARCHAR2(255),
   to_pos_type          VARCHAR2(255),
   to_pos_no            VARCHAR2(255),
   to_pos_channel_no    VARCHAR2(255),
   is_resort            VARCHAR2(255),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_PALLET_INNER_DETAIL primary key (id)
);

/*==============================================================*/
/* Table: fcs_pallet_move_detail                                */
/*==============================================================*/
create table fcs_pallet_move_detail 
(
   id                   CHAR(36)             not null,
   pallet_dispatch_id   CHAR(36),
   procedure_name       VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   pallet_status        VARCHAR2(255),
   form_no              VARCHAR2(255),
   pos_type             VARCHAR2(255),
   arrive_pos           VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_desc           VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FCS_PALLET_MOVE_DETAIL primary key (id)
);

/*==============================================================*/
/* Table: fsc_fm_pallet_split                                   */
/*==============================================================*/
create table fsc_fm_pallet_split 
(
   id                   CHAR(36)             not null,
   form_no              VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   equip_no             CHAR(36),
   pallet_cargo_id      CHAR(36),
   pallet_no            VARCHAR2(255),
   pallet_status        VARCHAR2(255),
   proc_start_time      DATE,
   proc_complete_time   DATE,
   split_policy         VARCHAR2(255),
   channel_policy       VARCHAR2(255),
   palletize_status     VARCHAR2(255),
   create_mode          VARCHAR2(255),
   fm_status            VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_desc           VARCHAR2(4000),
   remark               VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FSC_FM_PALLET_SPLIT primary key (id)
);

/*==============================================================*/
/* Table: fsc_fm_palletize                                      */
/*==============================================================*/
create table fsc_fm_palletize 
(
   id                   CHAR(36)             not null,
   form_no              VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   house_id             CHAR(36),
   house_no             VARCHAR2(255),
   equip_no             CHAR(36),
   pallet_no            VARCHAR2(255),
   pallet_cargo_id      CHAR(36),
   pallet_status        VARCHAR2(255),
   proc_start_time      DATE,
   proc_complete_time   DATE,
   channel_policy       VARCHAR2(255),
   palletize_status     VARCHAR2(255),
   create_mode          VARCHAR2(255),
   fm_status            VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_desc           VARCHAR2(4000),
   remark               VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FSC_FM_PALLETIZE primary key (id)
);

/*==============================================================*/
/* Table: fsc_fm_procedure                                      */
/*==============================================================*/
create table fsc_fm_procedure 
(
   id                   CHAR(36)             not null,
   form_no              VARCHAR2(255),
   house_no             VARCHAR2(255),
   house_id             CHAR(10),
   设备号                  VARCHAR2(255),
   pallet_cargo_id      CHAR(36),
   pallet_no            VARCHAR2(255),
   create_mode          VARCHAR2(255),
   work_procedure       VARCHAR2(255),
   pallet_status        VARCHAR2(255),
   loc_assign_time      DATE,
   in_loc_no            VARCHAR2(255),
   in_time              DATE,
   out_loc_no           VARCHAR2(255),
   out_plan_time        DATE,
   is_auto_out          INTEGER              default 0,
   out_time             DATE,
   stay_plan_time       INTEGER              default 0,
   stay_time            INTEGER              default 0,
   fm_status            VARCHAR2(255),
   error_code           VARCHAR2(255),
   error_msg            VARCHAR2(255),
   remark               VARCHAR2(4000),
   is_active            INTEGER              default 0,
   create_datetime      DATE,
   create_by            VARCHAR2(255),
   last_modify_datetime DATE,
   last_modify_by       VARCHAR2(255),
   constraint PK_FSC_FM_PROCEDURE primary key (id)
);

alter table fcs_alarm_handle_info
   add constraint FK_M_HANDLE_INFO_R_ALARM_INFO foreign key (alarm_id)
      references fcs_alarm_info (id);

alter table fcs_equipment_extension
   add constraint FK_ENT_EXTENSION_R__EQUIPMENT foreign key (equip_id)
      references fcs_equipment (id);

alter table fcs_pallet_inner_detail
   add constraint FK__INNER_DETAIL_R_T_DISPATCH foreign key (pallet_dispatch_id)
      references fcs_pallet_dispatch (id);

alter table fcs_pallet_move_detail
   add constraint FK_T_MOVE_DETAIL_R_T_DISPATCH foreign key (pallet_dispatch_id)
      references fcs_pallet_dispatch (id);
