alter table orders
    add constraint fk_orders_to_user
        foreign key (user_id)
            references user(id);

alter table event
    add constraint fk_event_to_artist
        foreign key (artist_id)
            references artist(id);

alter table ticket
    add constraint fk_ticket_to_event
        foreign key (event_id)
            references event(id);

alter table ticket
    add constraint fk_ticket_to_user
        foreign key (user_id)
            references user(id);

alter table order_detail
    add constraint fk_order_detail_to_orders
        foreign key (order_id)
            references orders(id);

alter table order_detail
    add constraint fk_order_detail_to_ticket
        foreign key (ticket_id)
            references ticket(id);