// ENUMS
Enum Role {
ADMIN
USER
}

Enum ApprovalStatus {
PENDING
APPROVED
REJECTED
}

Enum MatchingStatus {
REQUESTED
APPROVED
REJECTED
CANCELED
}

Enum AvailableStatus {
AVAILABLE
BOOKED
BLOCKED
}

Enum VerificationType {
QR
RECEIPT
NONE
}

Enum SpaceCategory {
PRACTICE_ROOM
STUDIO
MEETING_ROOM
PARTY_ROOM
CLASSROOM
POPUP_STORE
OFFICE
HALL
CAFE
OTHER
}

// USER AGGREGATE
Table Users {
id varchar [pk]
image_url varchar [note: "프로필 이미지 URL, 없으면 기본 이미지 사용"]
email varchar [unique, not null, note: "로그인 ID"]
password varchar
auth_provider varchar [default: 'LOCAL', note: "LOCAL, GOOGLE, KAKAO 등"]
provider_id varchar [note: "소셜 로그인 제공자 유저 식별자"]
name varchar [not null, note: "사용자 이름 또는 브랜드명"]
role Role [not null, note: "ADMIN, USER 구분"]
phone varchar [unique]
intro text
created_at datetime [default: `now()`]
updated_at datetime [default: `now()`]
deleted_at datetime
}

// SPACE AGGREGATE
Table Spaces {
id bigint [pk, increment]
host_id varchar [not null]
address_id bigint [not null]
name varchar [not null]
description text
ai_summary text
thumbnail_url varchar [not null]
price_per_hour int [not null]
admin_status ApprovalStatus [default: 'PENDING']
is_active boolean [default: true]
category SpaceCategory [not null]
phone varchar
created_at datetime [default: `now()`]
updated_at datetime [default: `now()`]
del

Table Addresses {
id bigint [pk, increment]
sido varchar [not null, note: "시/도"]
sigungu varchar [not null, note: "시/군/구"]
eup_myeon_dong varchar [note: "읍/면/동"]
road_address varchar [not null, note: "도로명 주소"]
jibun_address varchar [note: "지번 주소"]
detail_address varchar [note: "상세 주소"]
postal_code varchar [note: "우편번호"]
lat decimal(10, 8)
lng decimal(11, 8)
created_at datetime [default: `now()`]
updated_at datetime [default: `now()`]
}

Table Space_Images {
id bigint [pk, increment]
space_id bigint [not null]
image_url varchar [not null]
}

Table Space_Schedules {
id bigint [pk, increment]
space_id bigint [not null]
start_time datetime [not null]
end_time datetime [not null]
is_bookable boolean [default: true, note: "호스트가 해당 시간대 예약을 받는지 여부"]
}

// MATCHING AGGREGATE
Table Matchings {
id bigint [pk, increment]
space_id bigint [not null]
host_id varchar [not null]
seller_id varchar [not null]
start_time datetime [not null]
end_time datetime [not null]
total_price int [not null, note: "계약 성사 시점의 총 결제 금액 박제"]
status MatchingStatus [default: 'REQUESTED']
created_at datetime [default: `now()`]
updated_at datetime [default: `now()`]
}

Table Alarm {
id bigint [pk, increment]
user_id bigint [not null]
matching_id bigint [not null]
description text [not null]
}

// CHAT AGGREGATE
Table Chat_Rooms {
id bigint [pk, increment]
space_id bigint [not null]
host_id varchar [not null]
seller_id varchar [not null]
space_name varchar [not null, note: "채팅방 생성 시점의 공간 이름 비정규화 저장"]
host_name varchar [not null, note: "채팅방 생성 시점의 호스트 이름 비정규화 저장"]
seller_name varchar [not null, note: "채팅방 생성 시점의 셀러 이름 비정규화 저장"]
created_at datetime [default: `now()`]
}

Table Chat_Messages {
id bigint [pk, increment]
chat_room_id bigint [not null]
sender_id varchar [not null]
content text [not null]
is_read boolean [default: false, note: "메시지 읽음 여부 확인용"]
created_at datetime [default: `now()`]
}


// RELATIONSHIPS
Ref: Spaces.host_id > Users.id
Ref: Space_Images.space_id > Spaces.id
Ref: Space_Schedules.space_id > Spaces.id
Ref: Spaces.address_id > Addresses.id
Ref: Matchings.space_id > Spaces.id
Ref: Matchings.seller_id > Users.id
Ref: Matchings.host_id > Users.id
Ref: Matchings_Alarm.user_id > Users.id
Ref: Matchings_Alarm.matching_id > Matchings.id
Ref: Chat_Rooms.space_id > Spaces.id
Ref: Chat_Rooms.host_id > Users.id
Ref: Chat_Rooms.seller_id > Users.id
Ref: Chat_Messages.chat_room_id > Chat_Rooms.id
Ref: Chat_Messages.sender_id > Users.id


// TABLE GROUPS
TableGroup User_Aggregate {
Users
}
TableGroup Space_Aggregate {
Spaces
Space_Images
Space_Schedules
Addresses
}
TableGroup Matching_Aggregate {
Matchings
}