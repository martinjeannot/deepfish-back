package com.deepfish.aws.s3;

import java.net.URI;

public interface StaticResourceResolver {

  URI resolve(String targetURI);
}
