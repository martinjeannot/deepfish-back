package com.deepfish.talent.services;

import java.util.UUID;

public interface ReferralService {

  void sendInvitations(
      UUID talentId,
      String emails,
      String subject,
      String message
  );
}
