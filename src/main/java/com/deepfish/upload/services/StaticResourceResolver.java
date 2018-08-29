package com.deepfish.upload.services;

import java.net.URI;

public interface StaticResourceResolver {

  URI resolve(String targetURI);
}
