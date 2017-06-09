package uwyo.cs.acm2013.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import uwyo.cs.acm2013.dao.model.Compliance;
import uwyo.cs.acm2013.dao.model.Course;
import uwyo.cs.acm2013.dao.model.Degree;
import uwyo.cs.acm2013.dao.model.Foo;
import uwyo.cs.acm2013.dao.model.KnowledgeArea;
import uwyo.cs.acm2013.dao.model.KnowledgeUnit;
import uwyo.cs.acm2013.dao.model.LearningOutcome;
import uwyo.cs.acm2013.dao.model.Requirement;

public class Acm2013DAOImpl implements Acm2013DAO {
	
    @SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplate = null;
    
    public Acm2013DAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public List<Degree> getDegrees() {
		// TODO 2 - Get degrees from database (not hardcoded)
		
		
	    String sql = "SELECT degcode,degname FROM DEGREES";
	    List<Degree> foos = jdbcTemplate.query(sql, new RowMapper<Degree>() {
	 
	        @Override
	        public Degree mapRow(ResultSet rs, int rowNum) throws SQLException {
	            Degree foo = new Degree(rs.getString("DEGCODE"), rs.getString("DEGNAME"));
	            return foo;
	        }
	 
	    });
	 
		
		return foos;
	}
	@Transactional
	@Override
	public List<Requirement> getDegreeRequirementss( String degree) {
		// TODO 3 -  Get degree requirements from database (not hardcoded)
		final String degree1=degree;
		
		List<Requirement> reqs = new ArrayList<Requirement> ();
		String sql="select nc.options options from nocoursesreq nc,degrees d where nc.degcode=d.degcode and d.degcode=?";
		//PreparedStatement stmt1=myconn.prepareStatement(sql);
		//stmt1.setString(1,degree);
			final List<Integer> options;
			options=jdbcTemplate.query(sql,new PreparedStatementSetter() {
	            public void setValues(PreparedStatement preparedStatement) throws SQLException {
	                preparedStatement.setString(1, degree1);
	            }
	        },new RowMapper<Integer>(){
				public Integer mapRow(ResultSet rs,int row)throws SQLException{
					Integer option=new Integer(rs.getInt(1));
					return option;
			}}
					);
			for( int a=0;a<options.size();a++)
			{
				final int z=a;
				List<Course> reqcourses=new ArrayList<Course>();
				String sql1="Select cs.courseid courseid,cs.coursename coursename, cs.coursedesc coursedesc from courses cs,degreqs dr where dr.courseid=cs.courseid and dr.options=? and dr.degcode=?";
				reqcourses=jdbcTemplate.query(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		                preparedStatement.setInt(1, options.get(z));
		                preparedStatement.setString(2, degree1);
		            }}, new RowMapper<Course>(){
					
					public Course mapRow(ResultSet rs,int row)throws SQLException{
						Course course=new Course(rs.getString(1),rs.getString(2),rs.getString(3));
						return course;
					}
				});
				reqs.add(new Requirement(reqcourses));
			}
			
			
		
		return reqs;
	}
	@Transactional
	@Override
	public List<KnowledgeArea> getKnowledgeAreasFully() {
		// TODO 4 -  Get ACM curriculum from database (not hardcoded)
		List<KnowledgeArea> areas = new ArrayList<KnowledgeArea> ();
	    String sql = "SELECT KACODE,KANAME from KA";
	    
	    
	   final List<String> ka;
		ka=jdbcTemplate.query(sql,new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(1));
				return option;
		}});
		
	    final List<String> kaname;
		kaname=jdbcTemplate.query(sql,new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(2));
				return option;
		}});
		
		
		 for( int a=0;a<ka.size();a++)
		{
			 final int x=a;
			List<KnowledgeUnit> units = new ArrayList<KnowledgeUnit> ();
			
			
			String sql1="select kuid,ku,mintier1,mintier2 from KU where ku.ka=?";
			
			
			final List<Integer> kuid;
			kuid=jdbcTemplate.query(sql1,new PreparedStatementSetter() {
	            public void setValues(PreparedStatement preparedStatement) throws SQLException {
	                preparedStatement.setString(1, ka.get(x));
	            }},
	            new RowMapper<Integer>(){
				public Integer mapRow(ResultSet rs,int row)throws SQLException{
					Integer option=new Integer(rs.getInt(1));
					return option;
			}}
					);
			
			
			final List<Integer> tier1;
			tier1=jdbcTemplate.query(sql1,new PreparedStatementSetter() {
	            public void setValues(PreparedStatement preparedStatement) throws SQLException {
	                preparedStatement.setString(1, ka.get(x));
	            }},
	            new RowMapper<Integer>(){
				public Integer mapRow(ResultSet rs,int row)throws SQLException{
					Integer option=new Integer(rs.getInt(3));
					return option;
			}}
					);
			
			
			final List<Integer> tier2;
			tier2=jdbcTemplate.query(sql1,new PreparedStatementSetter() {
	            public void setValues(PreparedStatement preparedStatement) throws SQLException {
	                preparedStatement.setString(1, ka.get(x));
	            }},
	            new RowMapper<Integer>(){
				public Integer mapRow(ResultSet rs,int row)throws SQLException{
					Integer option=new Integer(rs.getInt(4));
					return option;
			}}
					);
			
			final List<String> kuname;
			kuname=jdbcTemplate.query(sql1,new PreparedStatementSetter() {
	            public void setValues(PreparedStatement preparedStatement) throws SQLException {
	                preparedStatement.setString(1, ka.get(x));
	            }},new RowMapper<String>(){
				public String mapRow(ResultSet rs,int row)throws SQLException{
					String option=new String(rs.getString(2));
					return option;
			}}
					);
			
			
			
			for(int b=0;b<kuid.size();b++)
			{
				final int y=b;
				
				List<LearningOutcome> outcomes=new ArrayList<LearningOutcome> ();
				String sql2="select lono,lo,lolevel from LO where kuid=?";
				
				
				final List<Integer> lono;
				lono=jdbcTemplate.query(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		                preparedStatement.setInt(1, kuid.get(y));
		            }},
		            new RowMapper<Integer>(){
					public Integer mapRow(ResultSet rs,int row)throws SQLException{
						Integer option=new Integer(rs.getInt(1));
						return option;
				}}
						);
				
				
				final List<String> lo;
				lo=jdbcTemplate.query(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setInt(1, kuid.get(y));
		            }},new RowMapper<String>(){
					public String mapRow(ResultSet rs,int row)throws SQLException{
						String option=new String(rs.getString(2));
						return option;
				}}
						);
				
				
				
				final List<String> lolevel;
				lolevel=jdbcTemplate.query(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setInt(1, kuid.get(y));
		            }},new RowMapper<String>(){
					public String mapRow(ResultSet rs,int row)throws SQLException{
						String option=new String(rs.getString(3));
						return option;
				}}
						);
				
				
				
				
				
				
				for(int c=0;c<lono.size();c++)				
				outcomes.add(new LearningOutcome(ka.get(a)+"/"+kuname.get(b)+"/"+lono.get(c),lo.get(c),lolevel.get(c).toLowerCase()));
				
				units.add (new KnowledgeUnit(ka.get(a)+"/"+kuname.get(b),kuname.get(b), tier1.get(b), tier2.get(b), outcomes));
				
				
			}
			
			areas.add(new KnowledgeArea(ka.get(a), kaname.get(a), units));
			
		}
		return areas;
	}

	@Override
	public Compliance getDegreeComplianceMap(String degree) {
		// TODO 5 -  Get compliance mapping for given degree from database (not hardcoded)
		
		final String degree1=degree;
		

		
		String sql="SELECT hrs.KA ka,   hrs.KU ku,   hrs.kuid kuid,   hrs.tier1hrs tier1hrs,   hrs.tier2hrs tier2hrs,   lo1.lono lono,   lo1.num num,   CASE lo1.lolevel     WHEN 'Familiarity'     THEN 'familiarity'     WHEN 'Assessment'     THEN 'assessment'     WHEN 'Usage'     THEN 'usage'     ELSE 'none'   END lolevel FROM   (SELECT SUM(tier1hrs) tier1hrs,     SUM(tier2hrs) tier2hrs,     KA,     KU,     KUID   FROM     (SELECT KA,       KU,       KUID,       MIN(tier1hrs) tier1hrs,       MIN(tier2hrs) tier2hrs     FROM       (SELECT *       FROM         (SELECT courses_kutaught.courseid,         courses_kutaught.kuid,         courses_kutaught.tier1hrs,         courses_kutaught.tier2hrs,           degreqs.degcode,           degreqs.options         FROM courses_kutaught         JOIN degreqs         ON DEGREQS.degcode  =?         AND degreqs.courseid=courses_kutaught.courseid         ORDER BY courses_kutaught.kuid         ) cilo NATURAL       LEFT OUTER JOIN nocoursesreq       ) cilo NATURAL right outer     JOIN ku     GROUP BY OPTIONS,       KUID,       KU,       KA     )   GROUP BY KUID,     KU,     KA     order by kuid   ) hrs,   (SELECT *   FROM     (SELECT MAX(num) num,       KUID,       KU,       KA,       lono     FROM       (SELECT MIN(num) num,         OPTIONS,         KUID,         KU,         KA,         lono       FROM         (SELECT *         FROM           (SELECT courses_info.courseid,             lo.kuid kuid,             lo.lono lono,             lo.lo lo,             courses_info.lolevel lolevel,             degreqs.degcode,             degreqs.options           FROM courses_info           JOIN degreqs           ON DEGREQS.degcode  =?           AND degreqs.courseid=courses_info.courseid           RIGHT OUTER JOIN LO           ON courses_info.kuid =lo.kuid           AND courses_info.lono=lo.lono           ORDER BY courses_info.kuid,             courses_info.lono           ) cilo NATURAL         LEFT OUTER JOIN nocoursesreq         ) cilo NATURAL       LEFT OUTER JOIN LOLEVEL NATURAL       JOIN ku NATURAL       LEFT OUTER JOIN courses_kutaught       GROUP BY OPTIONS,         KUID,         KU,         KA,         lono       )     GROUP BY KUID,       KU,       KA,       lono     ) NATURAL   LEFT OUTER JOIN LOLEVEL   ORDER BY KUID,     lono   ) lo1 WHERE hrs.kuid=lo1.kuid ORDER BY hrs.kuid,   lo1.lono";
		
		final List<String> kacode;
		kacode=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(1));
				return option;
		}}
				);
		
		final List<String> kuname;
		kuname=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(2));
				return option;
		}}
				);
		
		

		
		
		final List<Integer> kuid;
		kuid=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(3));
				return option;
		}}
				);
		
		
		
		final List<Integer> lono;
		lono=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(6));
				return option;
		}}
				);
		
		

		
		final List<Integer> tier1hrs;
		tier1hrs=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(4));
				return option;
		}}
				);
		
		
		final List<Integer> tier2hrs;
		tier2hrs=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(5));
				return option;
		}}
				);
		
		
		
		final List<String> lolevel;
		lolevel=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, degree1);
            	preparedStatement.setString(2, degree1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(8));
				return option;
		}}
				);
		
		
		
		
		
		Compliance compliance = new Compliance ();
	
		
		
		int a=0;
		int z=kuid.get(0);
		for( a=0;a<kacode.size();a++)
		{
			int y=kuid.get(a);
		/*	if(lolevel.get(a)==null)
				compliance.setComplianceOutcome(kacode.get(a)+"/"+kuname.get(a), kacode.get(a)+"/"+kuname.get(a)+"/"+lono.get(a), LearningOutcome.NONE);	
			else*/
			compliance.setComplianceOutcome(kacode.get(a)+"/"+kuname.get(a), kacode.get(a)+"/"+kuname.get(a)+"/"+lono.get(a), lolevel.get(a).toLowerCase());
			
			if(z!=y)
			{
				compliance.setComplianceTier(kacode.get(a-1)+"/"+kuname.get(a-1), 1, tier1hrs.get(a-1));
				compliance.setComplianceTier(kacode.get(a-1)+"/"+kuname.get(a-1), 2, tier2hrs.get(a-1));
				z=y;
				
			}
			
		}
		compliance.setComplianceTier(kacode.get(a-1)+"/"+kuname.get(a-1), 1, tier1hrs.get(a-1));
		compliance.setComplianceTier(kacode.get(a-1)+"/"+kuname.get(a-1), 2, tier2hrs.get(a-1));
		
		return compliance;
	}

	@Override
	public Course getCourseDetails(String code) {
		// TODO 6 -  Get information for given course from database (not hardcoded)
		final String code1=code;
		String sql="Select courseid,coursename,coursedesc from courses where courseid=?";
		//Course course=new course("")
		
		
		final List<String> courseid;
		courseid=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(1));
				return option;
		}}
				);
		
		
		final List<String> coursename;
		coursename=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(2));
				return option;
		}}
				);
		
		final List<String> coursedesc;
		coursedesc=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(3));
				return option;
		}}
				);
		
	    Course course=new Course(courseid.get(0),coursename.get(0),coursedesc.get(0));
		
		//Course course = new Course("4820", "Database Systems", "One course to rule them all");
		return course;
	}

	@Override
	public Compliance getCourseCompliance(String code) {
		// TODO 7 -  Get compliance mapping for given course from database (not hardcoded)
		final String code1=code;
		
		String sql="  SELECT ck.courseid courseid,   ka.kacode kacode,   ku.ku ku,   ck.tier1hrs tier1hrs,   ck.tier2hrs tier2hrs,   ci.lono lono,   ku.kuid kuid,     CASE ci.lolevel     WHEN 'Familiarity'     THEN 'familiarity'     WHEN 'Assessment'     THEN 'assessment'     WHEN 'Usage'     THEN 'usage'     ELSE 'none'     END lolevel FROM courses_info ci right outer join   courses_kutaught ck on ck.courseid=ci.courseid and ci.kuid=ck.kuid,     ku ku,   ka ka   where   ck.kuid      =ku.kuid   AND ka.kacode    =ku.ka   AND ck.courseid=?   ORDER BY ka.kacode,   ku.kuid,   ci.lono";
		
		Compliance compliance = new Compliance ();
		
		final List<String> courseid;
		courseid=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(1));
				return option;
		}}
				);
		
		
		final List<String> kacode;
		kacode=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(2));
				return option;
		}}
				);
		
		final List<String> ku;
		ku=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(3));
				return option;
		}}
				);
		
		
		final List<Integer> tier1hrs;
		tier1hrs=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(4));
				return option;
		}}
				);
		
		
		final List<Integer> tier2hrs;
		tier2hrs=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(5));
				return option;
		}}
				);
		
		
		
		final List<Integer> lono;
		lono=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(6));
				return option;
		}}
				);
		
		final List<Integer> kuid;
		kuid=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(7));
				return option;
		}}
				);
		
		
		final List<String> level;
		level=jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
            	preparedStatement.setString(1, code1);
            }},new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(8));
				return option;
		}}
				);
		
		
		
		int a=0;
		int z=kuid.get(0);
		for(a=0;a<kacode.size();a++)
		{
			int y=kuid.get(a);
			compliance.setComplianceOutcome(kacode.get(a)+"/"+ku.get(a), kacode.get(a)+"/"+ku.get(a)+"/"+lono.get(a), level.get(a).toLowerCase());
			if(z!=y)
			{
				compliance.setComplianceTier(kacode.get(a-1)+"/"+ku.get(a-1), 1, tier1hrs.get(a-1));
				compliance.setComplianceTier(kacode.get(a-1)+"/"+ku.get(a-1), 2, tier2hrs.get(a-1));
				z=y;
				
			}
			
		}
		compliance.setComplianceTier(kacode.get(a-1)+"/"+ku.get(a-1), 1, tier1hrs.get(a-1));
		compliance.setComplianceTier(kacode.get(a-1)+"/"+ku.get(a-1), 2, tier2hrs.get(a-1));
		return compliance;
	}

	@Override
	@Transactional
	public void setCoursecompliance(String code, Compliance compliance) throws org.springframework.dao.DataAccessException {
		// TODO 7 -  Set compliance mapping for given course into database
		final String code1=code;
		//Map<String, Compliance> course_compliance;
		//course_compliance = new HashMap<String,Compliance> ();
		//course_compliance.put(code, compliance);
		//compliance.compliance_map.put(code, (Map<String, Object>) compliance);
		String sql="select ka.kacode kacode,ku.ku ku,ku.kuid kuid,lo.lono lono from KA,KU,lo where ka.kacode=ku.ka and lo.kuid=ku.kuid order by ka.kacode,ku.kuid,lo.lono";
		
		
		final List<String> kacode;
		kacode=jdbcTemplate.query(sql,new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(1));
				return option;
		}}
				);
		
		final List<String> ku;
		ku=jdbcTemplate.query(sql,new RowMapper<String>(){
			public String mapRow(ResultSet rs,int row)throws SQLException{
				String option=new String(rs.getString(2));
				return option;
		}}
				);
		
		final List<Integer> kuid;
		kuid=jdbcTemplate.query(sql,new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(3));
				return option;
		}}
				);
		
		
		final List<Integer> lono;
		lono=jdbcTemplate.query(sql,new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs,int row)throws SQLException{
				Integer option=new Integer(rs.getInt(4));
				return option;
		}}
				);
		
		String level1;
		String sql3;
		int a;
		
		int z,y;
		for(a=0;a<kacode.size();a++)
		{
			z=0;
			y=0;
			final int l=compliance.getComplianceTier(kacode.get(a)+"/"+ku.get(a), 1);
			final int m=compliance.getComplianceTier(kacode.get(a)+"/"+ku.get(a), 2);
			level1=compliance.getComplianceOutcome(kacode.get(a)+"/"+ku.get(a), kacode.get(a)+"/"+ku.get(a)+"/"+lono.get(a));
			
			
		if(l==0 && m==0)
			{	
			
				final int c=kuid.get(a);
				final int d=lono.get(a);
				
				Object[] args={code1,c};
				int[] args1={java.sql.Types.VARCHAR,java.sql.Types.INTEGER};
				String sql1="delete from courses_info where courseid=? and kuid=?";
				z=jdbcTemplate.update(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	
		            }});
				
				

				
				Object[] args2={code1,c};
				int[] args3={java.sql.Types.VARCHAR,java.sql.Types.INTEGER};
				String sql2="delete from courses_kutaught where courseid=? and kuid=?";
				y=jdbcTemplate.update(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            }});
			}
			
			else
			{
				
				final int c=kuid.get(a);
				final int d=lono.get(a);
				final int tier1=compliance.getComplianceTier(kacode.get(a)+"/"+ku.get(a), 1);
				final int tier2=compliance.getComplianceTier(kacode.get(a)+"/"+ku.get(a), 2);
				
			/*	
			 * ----Code I have written for this scenario
			 * 
			 * 
			 * if("none".equals(level1))
				{
				
				Object[] args={code1,c,lono.get(a)};
				int[] args1={java.sql.Types.VARCHAR,java.sql.Types.INTEGER,java.sql.Types.INTEGER};
				
				String sql1="delete from courses_info where courseid=? and kuid=? and lono=?";
				
				z=jdbcTemplate.update(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, d);
		            }});
				
			//compliance.setComplianceOutcome(kacode.get(a)+"/"+ku.get(a), kacode.get(a)+"/"+ku.get(a)+"/"+lono.get(a), "none");
				
						
					String sql2="update courses_kutaught set tier1hrs=?,tier2hrs=? where courseid=? and kuid=?";		
					y=jdbcTemplate.update(sql2,new PreparedStatementSetter() {
				           public void setValues(PreparedStatement preparedStatement) throws SQLException {
				            	preparedStatement.setInt(1, tier1);
				            	preparedStatement.setInt(2, tier2);
				            	preparedStatement.setString(3, code1);
				            	preparedStatement.setInt(4, c);
				            	
				            }});
				
				
				
						if(y==0)
						{
						
						 sql3="insert into courses_kutaught values(?,?,?,?)";
							int x=jdbcTemplate.update(sql3,new PreparedStatementSetter() {
					            public void setValues(PreparedStatement preparedStatement) throws SQLException {
					            	preparedStatement.setString(1, code1);
					            	preparedStatement.setInt(2, c);
					            	preparedStatement.setInt(3, tier1);
					            	preparedStatement.setInt(4, tier2);
					            }});
						}
			}
			
		 */
			if(LearningOutcome.FAMILIARITY.equals(level1))
			{
			 	final String b=level1;


				String sql1="update courses_kutaught set tier1hrs=?,tier2hrs=? where courseid=? and kuid=?";
				z=jdbcTemplate.update(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setInt(1, tier1);
		            	preparedStatement.setInt(2, tier2);
		            	preparedStatement.setString(3, code1);
		            	preparedStatement.setInt(4, c);
		            	
		            }});
				
				String sql2="update courses_info set lolevel=? where courseid=? and kuid=? and lono=?";
				y=jdbcTemplate.update(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, "Familiarity");
		            	preparedStatement.setString(2, code1);
		            	preparedStatement.setInt(3, c);
		            	preparedStatement.setInt(4, d);
		            	
		            }});

				if(z==0)
				{
				 sql3="insert into courses_kutaught values(?,?,?,?)";
				int x=jdbcTemplate.update(sql3,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, tier1);
		            	preparedStatement.setInt(4, tier2);
		            }});
				
				}
				
				if(y==0)
				{
				
				String sql4="insert into courses_info values(?,?,?,?)";
				int v=jdbcTemplate.update(sql4,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, d);
		            	preparedStatement.setString(4,"Familiarity");
		            	
		            }});
				
				}
		
		 }
		 if(LearningOutcome.USAGE.equals(level1))
		 {
			 final String b=level1;
				String sql1="update courses_kutaught set tier1hrs=?,tier2hrs=? where courseid=? and kuid=?";
				z=jdbcTemplate.update(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setInt(1, tier1);
		            	preparedStatement.setInt(2, tier2);
		            	preparedStatement.setString(3, code1);
		            	preparedStatement.setInt(4, c);
		            	
		            }});
				
				String sql2="update courses_info set lolevel=? where courseid=? and kuid=? and lono=?";
				y=jdbcTemplate.update(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, "Usage");
		            	preparedStatement.setString(2, code1);
		            	preparedStatement.setInt(3, c);
		            	preparedStatement.setInt(4, d);
		            	
		            }});

				if(z==0)
				{
				 sql3="insert into courses_kutaught values(?,?,?,?)";
				int x=jdbcTemplate.update(sql3,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, tier1);
		            	preparedStatement.setInt(4, tier2);
		            }});
				}
				
				if(y==0)
				{
				
				String sql4="insert into courses_info values(?,?,?,?)";
				int v=jdbcTemplate.update(sql4,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, d);
		            	preparedStatement.setString(4, "Usage");
		            	
		            }});
				
				}
			 
		
		 }
		 
		 
		 if(LearningOutcome.ASSESSMENT.equals(level1))
		 {
			 final String b=level1;
				String sql1="update courses_kutaught set tier1hrs=?,tier2hrs=? where courseid=? and kuid=?";
				z=jdbcTemplate.update(sql1,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setInt(1, tier1);
		            	preparedStatement.setInt(2, tier2);
		            	preparedStatement.setString(3, code1);
		            	preparedStatement.setInt(4, c);
		            	
		            }});
				
				String sql2="update courses_info set lolevel=? where courseid=? and kuid=? and lono=?";
				y=jdbcTemplate.update(sql2,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, "Assessment");
		            	preparedStatement.setString(2, code1);
		            	preparedStatement.setInt(3, c);
		            	preparedStatement.setInt(4, d);
		            	
		            }});

				if(z==0)
				{
				 sql3="insert into courses_kutaught values(?,?,?,?)";
				int x=jdbcTemplate.update(sql3,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, tier1);
		            	preparedStatement.setInt(4, tier2);
		            }});
				}
				
				if(y==0)
				{
				
				String sql4="insert into courses_info values(?,?,?,?)";
				int v=jdbcTemplate.update(sql4,new PreparedStatementSetter() {
		            public void setValues(PreparedStatement preparedStatement) throws SQLException {
		            	preparedStatement.setString(1, code1);
		            	preparedStatement.setInt(2, c);
		            	preparedStatement.setInt(3, d);
		            	preparedStatement.setString(4, "Assessment");
		            	
		            }});
				
				}
		
		 }
		 
		}
		}
		

		

		

		Map<String, Compliance> course_compliance = new HashMap<String,Compliance> ();
		course_compliance.put(code, compliance);

	}

}
