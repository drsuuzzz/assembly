assembly
========

<p>This is a single cell agent-based model of BK virus (BKV) replicating within a salivary gland cell.
You'll see replication and transcription of viral proteins using host cell machinery.  The 3-D 
assembly of encapsidated BKV and non-infectious particles is also modeled. 
Capsid formation is modeled using boids.  How cool is that?</p>

<p>Context Builders:<br>

CytoNuc.java: Builds ABM with agents below.  Rules are scheduled on odd ticks.  Maintenance (add or
remove agents) is scheduled on even ticks.  This is because rules cannot be removed from the 
scheduler within a currently executing rule.</p>


<p>Agent Classes: Relating molecules involved in the process
<br>
DNAPol.java: Host<br>
Genome.java: Virus<br>
HostGenome.java: Host<br>
LgTAg.java: Virus<br>
MRNA.java: Host<br>
Ribosome.java: Host<br>
SmTAg.java: Virus<br>
TranscriptionFactor.java: Host<br>
VLP.java: Virus<br>
VP1.java: Virus<br>
VP123.java: Virus<br>
VP2.java: Virus<br>
VP3.java: Virus</p>

<p>General Agent support classes:<br>

AgentGeometry.java: A lot of methods are not used (yet) but implementation for geometric scaling, 
	rotations and transformations<br>
AgentMove.java: Move algorithms<br>
AgentProbabilities.java: All rule probabilities are in here<br></p>

<p>Repast Helper Files:
<br>
AgentStyle3D.java: Define colors of agents for display<br>
BoundToConverter.java: to convert enum class to string for proper display<br>
GStateConverter.java: ditto<br>
LocationConverter.java: ditto<br>
MStateConverter.java: ditto<br>
MTypeConverter.java: ditto<br>
MyContinuous3DProjectionDecorator.java: to facilitate drawing circles in projection<br>
MyContinuousProjectionDescriptor.java: ditto<br>
MyProjectionDecoratorFactory.java: ditto<br></p>
