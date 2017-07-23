/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.wire;

import org.mule.runtime.core.internal.transformer.simple.ByteArrayToMuleMessage;
import org.mule.runtime.core.internal.transformer.simple.MuleMessageToByteArray;

/**
 * Wire format using Java serialization to serialize Message objects accross the wire
 */
public class SerializedMuleMessageWireFormat extends TransformerPairWireFormat {

  public SerializedMuleMessageWireFormat() {
    setInboundTransformer(new ByteArrayToMuleMessage());
    setOutboundTransformer(new MuleMessageToByteArray());
  }
}
