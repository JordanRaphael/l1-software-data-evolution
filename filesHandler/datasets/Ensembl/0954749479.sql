# MySQL dump 5.13
#
# Host: localhost    Database: small
#--------------------------------------------------------
# Server version	3.22.22

#
# Table structure for table 'analysis'
#
CREATE TABLE analysis (
  db varchar(40),
  db_version varchar(5),
  program varchar(40) DEFAULT '' NOT NULL,
  program_version varchar(5),
  gff_source varchar(40),
  gff_feature varchar(40),
  id int(11) DEFAULT '0' NOT NULL auto_increment,
  PRIMARY KEY (id)
);

#
# Table structure for table 'clone'
#
CREATE TABLE clone (
  id varchar(40) DEFAULT '' NOT NULL,
  embl_id varchar(40) DEFAULT '' NOT NULL,
  version int(10) DEFAULT '0' NOT NULL,
  embl_version int(10) DEFAULT '0' NOT NULL,
  htg_phase int(10) DEFAULT '-1' NOT NULL,
  created datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  modified datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  stored datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  PRIMARY KEY (id)
);

#
# Table structure for table 'contig'
#
CREATE TABLE contig (
  id varchar(40) DEFAULT '' NOT NULL,
  internal_id int(10) DEFAULT '0' NOT NULL auto_increment,
  clone varchar(40) DEFAULT '' NOT NULL,
  mapbin varchar(40) DEFAULT '' NOT NULL,
  length int(10) unsigned,
  offset int(10) unsigned,
  orientation int(1) DEFAULT '1' NOT NULL,
  corder int(10) unsigned,
  dna int(10),
  PRIMARY KEY (internal_id),
  KEY clone_index (clone),
  KEY id_index (id)
);

#
# Table structure for table 'contig_equiv'
#
CREATE TABLE contig_equiv (
  id varchar(40) DEFAULT '' NOT NULL,
  from_contig_id varchar(40),
  from_contig_postion int(11),
  from_contig_strand char(1),
  to_contig_id varchar(40),
  to_contig_position int(11),
  to_contig_strand char(1),
  PRIMARY KEY (id)
);

#
# Table structure for table 'db_update'
#
CREATE TABLE db_update (
  id int(10) DEFAULT '0' NOT NULL auto_increment,
  time_started datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  time_finished datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  clones int(10) DEFAULT '0' NOT NULL,
  contigs int(10) DEFAULT '0' NOT NULL,
  genes int(10) DEFAULT '0' NOT NULL,
  exons int(10) DEFAULT '0' NOT NULL,
  basepairs int(10) DEFAULT '0' NOT NULL,
  features int(10) DEFAULT '0' NOT NULL,
  transcripts int(10) DEFAULT '0' NOT NULL,
  repeats int(10) DEFAULT '0' NOT NULL,
  supporting_features int(10) DEFAULT '0' NOT NULL,
  fsets int(10) DEFAULT '0' NOT NULL,
  status varchar(40) DEFAULT 'STARTED' NOT NULL,
  modified_start datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  modified_end datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  PRIMARY KEY (id)
);

#
# Table structure for table 'dna'
#
CREATE TABLE dna (
  sequence mediumtext NOT NULL,
  created datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  id int(10) unsigned DEFAULT '0' NOT NULL auto_increment,
  PRIMARY KEY (id)
);

#
# Table structure for table 'exon'
#
CREATE TABLE exon (
  id varchar(40) DEFAULT '' NOT NULL,
  contig varchar(40) DEFAULT '' NOT NULL,
  version int(10) DEFAULT '1' NOT NULL,
  created datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  modified datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  stored datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  seq_start int(10) DEFAULT '0' NOT NULL,
  seq_end int(10) DEFAULT '0' NOT NULL,
  strand int(2) DEFAULT '1' NOT NULL,
  phase int(11) DEFAULT '0' NOT NULL,
  end_phase int(11) DEFAULT '0' NOT NULL,
  KEY idx1 (id,contig),
  PRIMARY KEY (id),
  KEY contig_index (contig)
);

#
# Table structure for table 'exon_transcript'
#
CREATE TABLE exon_transcript (
  exon varchar(40) DEFAULT '' NOT NULL,
  transcript varchar(40) DEFAULT '' NOT NULL,
  rank int(10) DEFAULT '0' NOT NULL,
  PRIMARY KEY (exon,transcript,rank),
  KEY idx1 (exon,transcript),
  KEY exon_index (exon),
  KEY transcript_index (transcript)
);

#
# Table structure for table 'feature'
#
CREATE TABLE feature (
  id int(10) unsigned DEFAULT '0' NOT NULL auto_increment,
  contig varchar(40) DEFAULT '' NOT NULL,
  seq_start int(10) DEFAULT '0' NOT NULL,
  seq_end int(10) DEFAULT '0' NOT NULL,
  score int(10) DEFAULT '0' NOT NULL,
  strand int(1) DEFAULT '1' NOT NULL,
  analysis varchar(40) DEFAULT '' NOT NULL,
  name varchar(40),
  hstart int(11) DEFAULT '0' NOT NULL,
  hend int(11) DEFAULT '0' NOT NULL,
  hid varchar(40) DEFAULT '' NOT NULL,
  KEY overlap (id,contig,seq_start,seq_end,analysis),
  PRIMARY KEY (id),
  KEY contig_index (contig),
  KEY hid_index (hid)
);

#
# Table structure for table 'fset'
#
CREATE TABLE fset (
  id int(10) unsigned DEFAULT '0' NOT NULL auto_increment,
  score double(16,4) DEFAULT '0.0000' NOT NULL,
  PRIMARY KEY (id)
);

#
# Table structure for table 'fset_feature'
#
CREATE TABLE fset_feature (
  feature int(10) unsigned DEFAULT '0' NOT NULL,
  fset int(10) unsigned DEFAULT '0' NOT NULL,
  rank int(11) DEFAULT '0' NOT NULL,
  KEY feature_index (feature),
  KEY fset_index (fset),
  KEY feature_fset_index (feature,fset),
  PRIMARY KEY (feature,fset,rank)
);

#
# Table structure for table 'gene'
#
CREATE TABLE gene (
  id varchar(40) DEFAULT '' NOT NULL,
  version int(10) DEFAULT '1' NOT NULL,
  created datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  modified datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  stored datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  PRIMARY KEY (id)
);

#
# Table structure for table 'geneclone_neighbourhood'
#
CREATE TABLE geneclone_neighbourhood (
  clone varchar(40) DEFAULT '' NOT NULL,
  gene varchar(40) DEFAULT '' NOT NULL,
  PRIMARY KEY (clone,gene),
  KEY clone_index (clone),
  KEY gene_index (gene)
);

#
# Table structure for table 'ghost'
#
CREATE TABLE ghost (
  id varchar(40) DEFAULT '' NOT NULL,
  version varchar(5) DEFAULT '' NOT NULL,
  obj_type set('transcript','protein','exon') DEFAULT '' NOT NULL,
  deleted datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  stored datetime DEFAULT '0000-00-00 00:00:00' NOT NULL,
  PRIMARY KEY (id,version,obj_type)
);

#
# Table structure for table 'mapbin'
#
CREATE TABLE mapbin (
  id varchar(40) DEFAULT '' NOT NULL,
  chromosome char(2) DEFAULT '' NOT NULL,
  PRIMARY KEY (id)
);

#
# Table structure for table 'meta'
#
CREATE TABLE meta (
  donor_database_locator varchar(100) DEFAULT '' NOT NULL,
  offset_time time DEFAULT '00:30:00' NOT NULL,
  schema_version varchar(40) DEFAULT '' NOT NULL
);

#
# Table structure for table 'repeat_feature'
#
CREATE TABLE repeat_feature (
  id int(10) unsigned DEFAULT '0' NOT NULL auto_increment,
  contig varchar(40) DEFAULT '' NOT NULL,
  seq_start int(10) DEFAULT '0' NOT NULL,
  seq_end int(10) DEFAULT '0' NOT NULL,
  score int(10) DEFAULT '0' NOT NULL,
  strand int(1) DEFAULT '1' NOT NULL,
  analysis varchar(40) DEFAULT '' NOT NULL,
  hstart int(11) DEFAULT '0' NOT NULL,
  hend int(11) DEFAULT '0' NOT NULL,
  hid varchar(40) DEFAULT '' NOT NULL,
  KEY overlap (id,contig,seq_start,seq_end,analysis),
  PRIMARY KEY (id),
  KEY contig_index (contig),
  KEY hid_index (hid)
);

#
# Table structure for table 'supporting_feature'
#
CREATE TABLE supporting_feature (
  id int(10) unsigned DEFAULT '0' NOT NULL auto_increment,
  exon varchar(40) DEFAULT '' NOT NULL,
  seq_start int(10) DEFAULT '0' NOT NULL,
  seq_end int(10) DEFAULT '0' NOT NULL,
  score int(10) DEFAULT '0' NOT NULL,
  strand int(1) DEFAULT '1' NOT NULL,
  analysis varchar(40) DEFAULT '' NOT NULL,
  name varchar(40) DEFAULT '' NOT NULL,
  hstart int(11) DEFAULT '0' NOT NULL,
  hend int(11) DEFAULT '0' NOT NULL,
  hid varchar(40) DEFAULT '' NOT NULL,
  PRIMARY KEY (id),
  KEY overlap (id,seq_start,seq_end,analysis),
  KEY exon_id (id,exon),
  KEY exon_index (exon),
  KEY analysis_index (analysis),
  KEY hid_index (hid),
  KEY name_index (name)
);

#
# Table structure for table 'transcript'
#
CREATE TABLE transcript (
  id varchar(40) DEFAULT '' NOT NULL,
  version int(10) DEFAULT '1' NOT NULL,
  gene varchar(40) DEFAULT '' NOT NULL,
  translation varchar(40) DEFAULT '' NOT NULL,
  PRIMARY KEY (id),
  KEY id_geneid (id),
  KEY gene_index (gene)
);

#
# Table structure for table 'translation'
#
CREATE TABLE translation (
  id varchar(40) DEFAULT '' NOT NULL,
  version int(10) DEFAULT '1' NOT NULL,
  seq_start int(10) DEFAULT '0' NOT NULL,
  start_exon varchar(40) DEFAULT '' NOT NULL,
  seq_end int(10) DEFAULT '0' NOT NULL,
  end_exon varchar(40) DEFAULT '' NOT NULL,
  PRIMARY KEY (id)
);


create table contigoverlap (
       contig_a varchar(40) DEFAULT '' NOT NULL,
       contig_b varchar(40) DEFAULT '' NOT NULL,
       contig_a_position int(10) unsigned,
       contig_b_position int(10) unsigned,
       contig_a_version int(10) unsigned NOT NULL,
       contig_b_version int(10) unsigned NOT NULL,
       type varchar(40) DEFAULT '' NOT NULL,
       overlap_size int(10) unsigned,
       overlap_type set('right2left','left2right','left2left','right2right'),
       PRIMARY KEY(contig_a,contig_a_version,contig_b,contig_b_version,type)
  );
