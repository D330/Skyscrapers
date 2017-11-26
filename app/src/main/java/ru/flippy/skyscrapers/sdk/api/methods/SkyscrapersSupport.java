package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.request.support.SupportCreateTicketRequest;
import ru.flippy.skyscrapers.sdk.api.request.support.SupportRateTicketRequest;
import ru.flippy.skyscrapers.sdk.api.request.support.SupportTicketCommentRequest;
import ru.flippy.skyscrapers.sdk.api.request.support.SupportTicketRequest;
import ru.flippy.skyscrapers.sdk.api.request.support.SupportTicketsRequest;

public class SkyscrapersSupport {

    public SupportCreateTicketRequest createTicket(String type, String ticket) {
        return new SupportCreateTicketRequest(type, ticket);
    }

    public SupportTicketsRequest tickets() {
        return new SupportTicketsRequest(null);
    }

    public SupportTicketsRequest tickets(PaginationItem paginationItem) {
        return new SupportTicketsRequest(paginationItem);
    }

    public SupportTicketRequest ticket(long ticketId) {
        return new SupportTicketRequest(ticketId);
    }

    public SupportTicketCommentRequest ticketComment(long ticketId, String comment) {
        return new SupportTicketCommentRequest(ticketId, comment);
    }

    public SupportRateTicketRequest ticketRate(long ticketId, boolean fast, boolean resolved, boolean understand) {
        return new SupportRateTicketRequest(ticketId, fast, resolved, understand);
    }
}
