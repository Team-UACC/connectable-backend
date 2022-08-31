INSERT INTO artist(
    artist_image
    , artist_name
    , bank_account
    , bank_company
    , email
    , password
    , phone_number
    )
VALUE (
    "https://connectable.fans/_next/image?url=%2Fimages%2Fwelcome2.PNG&w=640&q=75"
    , "connectable"
    , "3333-03-9223680"
    , "카카오뱅크"
    , "hyunee169@sogang.ac.kr"
    , "sharecommon1!"
    , "010-5248-4170"
)
;

INSERT INTO event(
    contract_address
    , description
    , end_time
    , event_image
    , event_name
    , event_sales_option
    , instagram_url
    , location
    , sales_from
    , sales_to
    , start_time
    , twitter_url
    , webpage_url
    , artist_id
    , contract_name
)
VALUES (
    ""
    , "Connectable Welcome Event"
    , "2037-12-31 23:59:59"
    , "https://connectable.fans/_next/image?url=%2Fimages%2Fwelcome2.PNG&w=640&q=75"
    , "Connectable Welcome Event"
    , "FLAT_PRICE"
    , "https://connectable.fans/"
    , "서울특별시 성북구 정릉로 26길 8, 205동 1203호"
    , "2022-08-31 00:00:00"
    , "2037-12-31 23:59:59"
    , "2022-08-31 00:00:00"
    , "https://connectable.fans/"
    , "https://connectable.fans/"
    , 1
    , "Connectable"
)
;