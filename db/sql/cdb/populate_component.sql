LOCK TABLES `component` WRITE;
/*!40000 ALTER TABLE `component` DISABLE KEYS */;
INSERT INTO `component` VALUES
(1,'Gate Valve (RF Shielded)','Ag-coated SST liner, 22 mm ID, standard SST 2 3/8\\ CF flanges\"',75,1),
(2,'Gate Valve (xray extraction)','Ag-coated SST liner, 22 mm ID with pocket for x-ray extraction, standard 2 3/8\\ SST CF flanges\"',75,2),
(3,'VacChamberAssy1','GTAW-welded SST 316L tube, SST 316L standard and chain-clamped CF flanges',64,3),
(4,'VacChamberAssy2','GTAW-welded SST 316L tube, SST 316L standard and chain-clamped CF flanges',64,4),
(5,'VacChamberAssy3','GTAW-welded aluminum extrusion with antechamber, AL chain-clamped CF flanges',64,5),
(6,'VacChamberAssy4','GTAW-welded, EDM-ed aluminum with side pocket for x-ray extraction, AL chain-clamped CF flanges',64,6),
(7,'VacChamberAssy5','GTAW-welded, EDM-ed aluminum with side pocket for x-ray extraction, AL chain-clamped CF flanges',64,7),
(8,'VacChamberAssy6','GTAW-welded SST 316L cross, SST 316L chain-clamped flanges',64,8),
(9,'VacChamberAssy7','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,9),
(10,'VacChamberAssy8','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,10),
(11,'VacChamberAssy9','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,11),
(12,'VacChamberAssy10','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,12),
(13,'VacChamberAssy11','GTAW-welded aluminum extrusion with antechamber, AL chain-clamped CF flanges',64,13),
(14,'VacChamberAssy12','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,14),
(15,'VacChamberAssy13','E-beam welded Cu extrusion with internal NEG coating, bi-metal Cu-SST316L flanges',64,15),
(16,'VacChamberAssy14','E-beam welded Cu extrusion with internal NEG coating, bi-metal Cu-SST316L flanges',64,16),
(17,'VacChamberAssy15','E-beam welded Cu extrusion with internal NEG coating, bi-metal Cu-SST316L flanges',64,17),
(18,'VacChamberAssy16','E-beam welded Cu extrusion with internal NEG coating, bi-metal Cu-SST316L flanges',64,18),
(19,'VacChamberAssy17','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,19),
(20,'VacChamberAssy18','GTAW-welded aluminum extrusion with antechamber, AL chain-clamped CF flanges',64,20),
(21,'VacChamberAssy19','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,21),
(22,'VacChamberAssy20','GTAW-welded aluminum extrusion without antechamber, AL chain-clamped CF flanges',64,22),
(23,'VacChamberAssy21','GTAW-welded aluminum extrusion with antechamber, AL chain-clamped CF flanges',64,23),
(24,'VacChamberAssy22','GTAW-welded SST316L rectandular tube, standard SST316L 2 3/4\\ CF flanges\"',64,24),
(25,'VacChamberAssy23','GTAW-welded SST316L rectandular tube, standard SST316L 2 3/4\\ CF flanges\"',64,25),
(26,'VacChamberAssy24','GTAW-welded SST316L rectandular tube, standard SST316L 2 3/4\\ CF flanges\"',64,26),
(27,'VacChamberAssy25','GTAW-welded SST316L rectandular tube, standard SST316L 2 3/4\\ CF flanges\"',64,27),
(28,'BpmRfShieldedBelAssy1','Bolted assembly of furnace-brazed Cu and BeCu liner, and SST 316L edge-welded bellows with SST 316L CF chain-clamped flanges',87,28),
(29,'WedgeAbsorber1','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,29),
(30,'WedgeAbsorber2','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,30),
(31,'WedgeAbsorber3','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,31),
(32,'WedgeAbsorber4','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,32),
(33,'CrotchAbsorber1','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,33),
(34,'CrotchAbsorber2','Furnace-brazed Glidcop, copper tube, and SST 304 CF flange',67,34),
(35,'VacGgTreeAssy1','Bolted assembly of vendor items, SST 304 chambers, standard SST 304 CF flanges',72,35),
(36,'Ion Pump (Gamma)','Vendor item, Ti/Ta electrodes with standard SST 304 CF flange',66,36),
(37,'RGA (Ametek)','Residual Gas Analyzer',21,37),
(38,'IonPumpCtlr1','Rack-mountable chasis',11,38),
(39,'IonPumpHvCable1','Copper conductor with radiation-resistant insulation',34,39),
(40,'VacGaugeCtlr1','Rack-mountable chasis',13,40),
(41,'ConvGaugeCable1','Copper conductor with radiation-resistant insulation',34,41),
(42,'ColdCathodeGaugeCable1','Copper conductor with radiation-resistant insulation',34,42),
(43,'RgaCable1','Copper conductor with radiation-resistant insulation',34,43),
(45,'GateValveCable1','Copper conductor with radiation-resistant insulation',34,45),
(46,'DiWaterMontoringSys1','Cabinet with V-cones and transmitters, RTD transmitters, pressure monitoring, shut-off valves, etc',22,46),
(47,'VacChamberHeatTapeCtlr1','Rack-mountable chasis',14,47),
(48,'VacChamberHeatTapeCable1','Copper conductor with radiation-resistant insulation',34,48),
(49,'TransAbsorber1','',67,49),
(50,'TransAbsorber2','',67,50),
(55,'Generic: Rack','',5,55),
(56,'Generic: Cabinet',NULL,6,56),
(57,'Generic: Enclosure',NULL,7,57),
(58,'Generic: Card Cage',NULL,8,58),
(59,'Generic: Instrumentation Component',NULL,9,59),
(60,'Generic: Controller - Generic',NULL,10,60),
(61,'Generic: Controller - Ion Pump',NULL,11,61),
(62,'Generic: Controller - Gate Valve',NULL,12,62),
(63,'Generic: Controller - Vacuum Gauge',NULL,13,63),
(64,'Generic: Controller - Heat tape',NULL,14,64),
(65,'Generic: Controller - PID',NULL,15,65),
(66,'Generic: Controller - Temperature',NULL,16,66),
(67,'Generic: Controller - Motor',NULL,17,67),
(68,'Generic: Controller - Power Supply',NULL,18,68),
(69,'Generic: Controller - PLC',NULL,19,69),
(70,'Generic: Controller - Water flow',NULL,20,70),
(71,'Generic: Controller - RGA',NULL,21,71),
(72,'Generic: Monitoring System',NULL,22,72),
(73,'Generic: Gauge/Sensor - strain',NULL,23,73),
(74,'Generic: Gauge/Sensor  - vacuum',NULL,24,74),
(75,'Generic: Gauge/Sensor  - thermocouple',NULL,25,75),
(76,'Generic: Gauge/Sensor  - RTD',NULL,26,76),
(77,'Generic: Gauge/Sensor  - pressure',NULL,27,77),
(78,'Generic: Gauge/Sensor - waterflow',NULL,28,78),
(79,'Generic: Gauge/Sensor - RGA',NULL,29,79),
(80,'Generic: Motor',NULL,30,80),
(81,'Generic: Motor - Driver',NULL,31,81),
(82,'Generic: Motor - Position Monitor',NULL,32,82),
(83,'Generic: Motor - Limit Switch',NULL,33,83),
(84,'Generic: Cable',NULL,34,84),
(85,'Generic: Patch Panel',NULL,35,85),
(86,'Generic: Adapter',NULL,36,86),
(87,'Generic: Module',NULL,37,87),
(88,'Generic: Blackbox',NULL,38,88),
(89,'Generic: ADC',NULL,39,89),
(90,'Generic: DAC',NULL,40,90),
(91,'Generic: Discrete I/O',NULL,41,91),
(92,'Generic: CPU',NULL,42,92),
(93,'Generic: FPGA',NULL,43,93),
(94,'Generic: Oscilloscope/DSA',NULL,44,94),
(95,'Generic: Counter',NULL,45,95),
(96,'Generic: Function Generator',NULL,46,96),
(97,'Generic: Frequency Synthesizer',NULL,47,97),
(98,'Generic: Voltmeter',NULL,48,98),
(99,'Generic: Power Supply',NULL,49,99),
(100,'Generic: Amplifier',NULL,50,100),
(101,'Generic: Multiplexor',NULL,51,101),
(102,'Generic: Interlock',NULL,52,102),
(103,'Generic: Readout/Display',NULL,53,103),
(104,'Generic: Controls Component',NULL,54,104),
(105,'Generic: Network',NULL,55,105),
(106,'Generic: Timing',NULL,56,106),
(107,'Generic: IOC',NULL,57,107),
(108,'Generic: Server/Workstation','',58,108),
(109,'Generic: Video',NULL,59,109),
(110,'Generic: Interface Adapter',NULL,60,110),
(111,'Generic: Accelerator Component',NULL,61,111),
(112,'Generic: Girder',NULL,62,112),
(113,'Generic: Stand',NULL,63,113),
(114,'Generic: Vacuum Chamber',NULL,64,114),
(115,'Generic: Transition Piece',NULL,65,115),
(116,'Generic: Vacuum Pump',NULL,66,116),
(117,'Generic: Absorber',NULL,67,117),
(118,'Generic: Heat Tape',NULL,68,118),
(119,'Generic: Flag',NULL,69,119),
(120,'Generic: Scraper',NULL,70,120),
(121,'Generic: Bellows',NULL,71,121),
(122,'Generic: Assembly',NULL,72,122),
(123,'Generic: Vacuum Flange',NULL,73,123),
(124,'Generic: Vacuum Seal',NULL,74,124),
(125,'Generic: Vacuum Valve',NULL,75,125),
(126,'Generic: Fastener',NULL,76,126),
(127,'Generic: Water line',NULL,77,127),
(128,'Generic: Water fitting',NULL,78,128),
(129,'Generic: Water seal',NULL,79,129),
(130,'Generic: Magnet','',80,130),
(131,'Generic: Trim',NULL,81,131),
(132,'Generic: Dipole',NULL,82,132),
(133,'Generic: Quadrupole','',83,133),
(134,'Generic: Sextupole','',84,134),
(135,'Generic: PS Component',NULL,85,135),
(136,'Generic: Diagnostic Component',NULL,86,136),
(137,'Generic: BPM',NULL,87,137),
(138,'Generic: Loss Monitor',NULL,88,138),
(139,'Generic: Current Monitor',NULL,89,139),
(140,'Generic: Screen',NULL,90,140),
(141,'Generic: Optics',NULL,91,141),
(142,'Generic: RF Component',NULL,92,142),
(143,'Generic: cavity/accelerating structure',NULL,93,143),
(144,'Generic: phase shifter',NULL,94,144),
(145,'Generic: attenuator',NULL,95,145),
(146,'Generic: coupler',NULL,96,146),
(147,'Generic: envelope detector',NULL,97,147),
(148,'Generic: phase monitor/detector',NULL,98,148),
(149,'Generic: klystron',NULL,99,149),
(150,'Generic: HVPS',NULL,100,150),
(151,'Generic: splitter',NULL,101,151),
(152,'Generic: RF Source',NULL,102,152),
(153,'Generic: circulator',NULL,103,153),
(154,'Generic: Beamline Component',NULL,104,154),
(155,'Generic: Insertion Device Component',NULL,105,155),
(156,'VME Chassis - 7 slot','Table top 7 slot VME chassis',8,165),
(157,'EVR100','APS Event Receiver',56,166),
(158,'VMIVME-5588DMA','VME Reflective Memory Board',37,167),
(159,'MVME6100 CPU','VME CPU',42,168),
(160,'Generic: Quad Bottom Half','',83,178),
(161,'Generic: Quad Top Half','',83,179),
(162,'Generic: Sextupole Top Half','',84,180),
(163,'Generic: Sextupole Bottom Half','',84,181),
(164,'Generic: Plinth','',119,182),
(165,'Generic: Plate','',118,183),
(166,'Q6','Quadrupole 6 Assembly',83,185),
(167,'S1','Sextupole 1 Design',84,186),
(168,'DMM Base Plate','',118,187),
(169,'Quadrupole 0.569m','',83,188),
(170,'Quadrupole 0.401m','',83,189),
(171,'Quadrupole 0.259m','',83,190),
(172,'Quadrupole 0.213m','',83,191),
(173,'Sextupole 0.186m','',84,192),
(174,'Sextupole 0.248m','',84,193),
(175,'CSBEND','',115,203),
(176,'KQUAD','',113,204),
(177,'KSEXT','',117,205),
(178,'MONI','',114,206),
(179,'VERTEX-POINT','',116,208),
(180,'Generic: Laptop/Tablet','',121,382),
(181,'XEM7350-K70T','Xilinx Kintex-7 board with USB 3.0',43,388),
(182,'MULTIPLET R&D MOLD CAVITY ASSEMBLY','U221020202-104210',61,390),
(183,'FID HV Pulser for MBA Injection','Model #FPG 20-01MC2N6',122,392),
(184,'DSO-S204A Oscilloscope','Infiniium S Series 2 GHz 4 channel\r\n',44,393),
(185,'ATF HV Attenuator','Model # 20-025N\r\n',123,394),
(186,'FID High Voltage Coax Cable','Describe length in Instance Property ',34,395),
(187,'DMM Plinth','',119,427),
(191,'Schroff MicroTCA Chassis','Model 11850-026',7,440),
(192,'MIcroTCA.4 Power Supply','AC-DC 1000W power supply (double height, double width) for an  MTCA.4 chassis. 12Vdc and 3.3Vdc output, low noise/low ripple. ',49,441),
(193,'MicroTCA Carrier Hub (MCH) ','',10,442),
(194,'AM31x MicroTCA CPU','AMC Single-Width mid-height front panel 2nd Gen Intel Core CPU  w/2 core 2.2GHz Corei7 2655LE with 4GB DRAM dual x4 PCI  Express Fat Pipes. Extended Options Region I/O = GPIO, 2xUSB,  1xRS232 and 2xSATA. Without Display Port connector. ',42,443),
(195,'8 Gigabyte Flash Disk Module for AM31x','',124,445),
(196,'AMC-V7-2C6678','FPGA/DSP MicroTCA Board',43,446);
/*!40000 ALTER TABLE `component` ENABLE KEYS */;
UNLOCK TABLES;
