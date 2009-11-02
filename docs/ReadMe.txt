This is a single cell agent-based model of BK virus (BKV) replicating within a salivary gland cell.
You'll see replication and transcription of viral proteins using host cell machinery.  The 3-D 
assembly of encapsidated BKV and non-infectious particles is also modeled.

Context Builders:

CytoNuc.java: Builds ABM with agents below.  Rules are scheduled on odd ticks.  Maintenance (add or
remove agents) is scheduled on even ticks.  This is because rules cannot be removed from the 
scheduler within a currently executing rule.


Agent Classes: Relating molecules involved in the process

DNAPol.java: Host
Genome.java: Virus
HostGenome.java: Host
LgTAg.java: Virus
MRNA.java: Host
Ribosome.java: Host
SmTAg.java: Virus
TranscriptionFactor.java: Host
VLP.java: Virus
VP1.java: Virus
VP123.java: Virus
VP2.java: Virus
VP3.java: Virus

General Agent support classes:

AgentGeometry.java: A lot of methods are not used (yet) but implementation for geometric scaling, 
	rotations and transformations
AgentMove.java: Move algorithms
AgentProbabilities.java: All rule probabilities are in here

Repast Helper Files:

AgentStyle3D.java: Define colors of agents for display
BoundToConverter.java: to convert enum class to string for proper display
GStateConverter.java: ditto
LocationConverter.java: ditto
MStateConverter.java: ditto
MTypeConverter.java: ditto
MyContinuous3DProjectionDecorator.java: to facilitate drawing circles in projection
MyContinuousProjectionDescriptor.java: ditto
MyProjectionDecoratorFactory.java: ditto