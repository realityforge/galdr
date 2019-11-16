/**
 * Core Galdr primitives.
 */
@OmitPattern( symbol = "^\\$clinit$" )
@OmitPattern( type = "^.*\\.Galdr_[^.]+$", symbol = "^\\$clinit$" )
@OmitPattern( type = "^.*\\.Galdr_[^.]+$", symbol = "^toString$", unless = "galdr.debug_to_string" )
package galdr;

import grim.annotations.OmitPattern;
