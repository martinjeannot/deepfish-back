package com.deepfish.interview.domain;

/**
 * RFC 5445 equivalent : Participation Status
 *
 * See https://tools.ietf.org/html/rfc5545#section-3.2.12
 */
public enum ParticipationStatus {
  NEEDS_ACTION, // default
  ACCEPTED,
  DECLINED,
  TENTATIVE,
}
