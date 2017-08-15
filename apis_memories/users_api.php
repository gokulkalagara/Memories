<?php
require_once("Rest.inc.php");

	
class USERS_API extends REST
{

	
		
		public $data = "";
		const DB_SERVER = "localhost";
		const DB_USER = "root";
		const DB_PASSWORD = "";
		const DB = "db_memories";
		
		private $db = NULL;

		
	
		public function __construct()
		{
			parent::__construct();				// Init parent contructor
			$this->dbConnect();	

			// Initiate Database connection
		}



		// db connect by class object create
		private function dbConnect()
		{
			$this->db = mysqli_connect(self::DB_SERVER,self::DB_USER,self::DB_PASSWORD,self::DB);	
		}


		// process api
		public function processApi()
		{

			if(!empty($_REQUEST['request']))
			{
			
				$path=explode("/",strtolower($_REQUEST['request']));
				if(is_array($path))
				{
				    if(count($path)>=2)
					{

						if(strcmp("maya",$path[0])==0)
						{
							if((int)method_exists($this,$path[1]) > 0)
							{
							//if(!empty($func[]))
							$func=$path[1];
							$this->$func();
							}
							else
							{
							$this->dbClose();
							$this->response('',404);
							}
						}
						else
						{
							$this->dbClose();
							$this->response('',404);
						}
					}
					else
					{
					     
							$this->dbClose();
							$this->response('',404);
					}
				}
				else
				{
	        	$this->dbClose();
				$this->response('',404);
				}

			}
			else
			{
				$this->dbClose();
				$this->response('',404);
			}
			$this->dbClose();
		}

		// db close
		private function dbClose()
		{
			mysqli_close($this->db);
		}

		// add user
		private function adduser()
		{
		  $variables=json_decode(file_get_contents("php://input"),true);
		  //echo $variables["profile_pic"];exit();
		  if(!empty($variables)&&count($variables)==7)
		  {
		  $result=mysqli_query($this->db,"CALL get_user_record('".$variables["first_name"]."','".$variables["last_name"]."','".$variables["email"]."','".$variables["login_id"]."','".$variables["profile_pic"]."','".$variables["gender"]."','".$variables["dob"]."')");
				if(mysqli_num_rows($result) > 0) // it reflect signle record;
				{
					$userDetails = mysqli_fetch_array($result,MYSQLI_ASSOC);
					header('Content-Type: application/json');
					$success["status"]=true;
					$success["message"]="valid operation";
					$success["data"]=$userDetails;
					$this->response($this->json($success), 200);
				}
			   else
			   {
				   $fail["status"]=false;
				   $fail["message"]="no data found operation";
				   $this->response($this->json($fail),200);
			   }
		  }
		  else
		  {
				$this->dbClose();
				$this->response('',404);
		  }

		 $this->dbClose();
		}

		// add mobile details with fcm token, imei, UUID; // imei -> device_id
		private function addfcm()
		{
			 $variables=json_decode(file_get_contents("php://input"),true);
		  //echo $variables["profile_pic"];exit();
		  if(!empty($variables)&&count($variables)==4)
		  {
			  //cuugvJZM3gY:APA91bEw3LsnUDQzlwe_Ssu68JLScVkRl6x0psKFyh3lZikI2knDe7cDTTqdJ15PsfpZkK9s3huEAjmwshUidmY2mcZ99n-HGKN-uMqsTvdu55liRsSF9zQIwrxdTTtNZzv0J7qJs4Pw
			  
			  $result=mysqli_query($this->db,"CALL insert_or_update_users_tokens('".$variables["imei"]."','".$variables["fcm_id"]."','".$variables["platform"]."','".$variables["u_uid"]."')");
		   if($result) // it reflect signle record;
		   {
				header('Content-Type: application/json');
				$success["status"]=true;
				$success["message"]="successfully fcm is updated";
				//$success["data"]="successfully fcm is updated";
				$this->response($this->json($success), 200);
		   }
		   else
		   {
		   $fail["status"]=false;
		   $fail["message"]="no data found operation";
		   $this->response($this->json($fail),200);
		   }
		  }
		  else
		  {
			  	$this->dbClose();
				$this->response('',404);
		  }

		 $this->dbClose();
		}

		// get fcm records of user based on U_UID;
		private function getfcm()
		{
	      $variables=json_decode(file_get_contents("php://input"),true);
		  //echo $variables["u_uid"];exit();
		  if(!empty($variables)&&count($variables)==1)
		  {
		   $result=mysqli_query($this->db,"CALL get_fcm_records_of_users('".$variables["u_uid"]."')");
		   if(mysqli_num_rows($result) > 0) // it reflect signle record;
		   {
				$userDetails=array();
				while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
			    {
						array_push($userDetails, $row);
				}
				header('Content-Type: application/json');
				$success["status"]=true;
				$success["message"]="users fcm list";
				$success["data"]=$userDetails;
				$this->response($this->json($success), 200);
		   }
		   else
		   {
		   header('Content-Type: application/json');
		   $fail["status"]=false;
		   $fail["message"]="no data found operation";
		   $this->response($this->json($fail),200);
		   }
		  }
		  else
		  {
			  	$this->dbClose();
				$this->response('',404);
		  }

		 $this->dbClose();
		}


		// fetch users list based on u_uid;
		private function fetchusers()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			//echo $variables["u_uid"];exit();
			if(!empty($variables)&&count($variables)==1)
			{
				$result=mysqli_query($this->db,"CALL fetch_users_list('".$variables["u_uid"]."')");
				if(mysqli_num_rows($result) > 0) // it reflect signle record;
				{
						$userDetails=array();
						while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
						{
						array_push($userDetails, $row);
						}
						header('Content-Type: application/json');
						$success["status"]=true;
						$success["message"]="user list";
						$success["data"]=$userDetails;
						$this->response($this->json($success), 200);
				}
				else
				{
						header('Content-Type: application/json');
						$fail["status"]=false;
						$fail["message"]="no data found operation";
						$this->response($this->json($fail),200);
				}

			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}
		  
		}

		// fetch class mates
		private function fetchclassmates()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			//echo $variables["u_uid"];exit();
			if(!empty($variables)&&count($variables)==1)
			{
				$result=mysqli_query($this->db,"CALL get_class_mates()");
				if(mysqli_num_rows($result) > 0) // it reflect signle record;
				{
						$userDetails=array();
						while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
						{
						array_push($userDetails, $row);
						}
						header('Content-Type: application/json');
						$success["status"]=true;
						$success["message"]="class list";
						$success["data"]=$userDetails;
						$this->response($this->json($success), 200);
				}
				else
				{
						header('Content-Type: application/json');
						$fail["status"]=false;
						$fail["message"]="no data found operation";
						$this->response($this->json($fail),200);
				}

			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}
		  
		}



		// fetch class mate photos
		private function fetchphotos()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			//echo $variables["u_uid"];exit();
			if(!empty($variables)&&count($variables)==1)
			{
				$result=mysqli_query($this->db,"CALL get_class_mate_photos('".$variables["roll_number"]."')");
				if(mysqli_num_rows($result) > 0) // it reflect signle record;
				{
						$userDetails=array();
						while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
						{
						array_push($userDetails, $row);
						}
						header('Content-Type: application/json');
						$success["status"]=true;
						$success["message"]="classmate Photos";
						$success["data"]=$userDetails;
						$this->response($this->json($success), 200);
				}
				else
				{
						header('Content-Type: application/json');
						$fail["status"]=false;
						$fail["message"]="no data found operation";
						$this->response($this->json($fail),200);
				}

			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}
		  
		}

		// fetch class mate photos
		private function fetchvideos()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			//echo $variables["u_uid"];
			if(!empty($variables)&&count($variables)==1)
			{
				$result=mysqli_query($this->db,"CALL get_class_mates_videos()");
				if(mysqli_num_rows($result) > 0) // it reflect signle record;
				{
						$userDetails=array();
						while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
						{
						array_push($userDetails, $row);
						}
						header('Content-Type: application/json');
						$success["status"]=true;
						$success["message"]="classmates videos";
						$success["data"]=$userDetails;
						$this->response($this->json($success), 200);
				}
				else
				{
						header('Content-Type: application/json');
						$fail["status"]=false;
						$fail["message"]="no data found operation";
						$this->response($this->json($fail),200);
				}

			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}
		  
		}


		//222-> notify receieve a message based on u_uid
		private function send_message()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			if(!empty($variables)&&count($variables)>=10)
			{
							$result=mysqli_query($this->db,"CALL get_fcm_records_of_users('".$variables["u_uid"]."')");
							if(mysqli_num_rows($result) > 0) // it reflect signle record;
							{
							$userDetails=array();
							while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
							{
							array_push($userDetails, $row["fcm_token"]);
							}
									$data = array(
									'title'=>$variables["title"],
									'message'=>$variables["message"],
									'color'=>$variables["color"],
									'summary'=>$variables["summary"],
									'big_text'=>$variables["big_text"],
									'nofitication_id'=>$variables["nofitication_id"],
									'big_allow_icon'=>$variables["big_allow_icon"],
									'url'=>"".$variables["url"],
									'type'=>$variables["type"],
									'u_uid'=>$variables["u_uid"],
									'm_u_uid'=>$variables["m_u_uid"]);

							
														// transfer data to FCM SERVER
														$target=$userDetails;
														$url = 'https://fcm.googleapis.com/fcm/send';
														//api_key available in Firebase Console -> Project Settings -> CLOUD 
														$server_key = 'AIzaSyBFf_si6NMSuUzu-OZZ2p6r_mCrKNrxnN4';

														$fields = array();
														if(is_array($target)){
														$fields['registration_ids'] = $target;
														}else{
														$fields['to'] = $target;
														}
														$fields['data'] = $data;

														//header with content_type api key
														$headers = array(
														'Content-Type:application/json',
														'Authorization:key='.$server_key
														);
														$ch = curl_init();
														curl_setopt($ch, CURLOPT_URL, $url);
														curl_setopt($ch, CURLOPT_POST, true);
														curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
														curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
														curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
														curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
														curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
														//echo json_encode($fields);
														$result = curl_exec($ch);
														curl_close($ch);
														if ($result === FALSE) 
														{
																header('Content-Type: application/json');
																$fail["status"]=false;
																$fail["message"]="no data found operation";
																$this->response($this->json($fail),200);
																$this->dbClose();
														}
														else
														{
															    header('Content-Type: application/json');
																$success["status"]=true;
																$success["message"]="response attached to data";
																$success["data"]=json_decode($result);
																$this->response($this->json($success),200);
																$this->dbClose();
														}

							}
							else
							{
							header('Content-Type: application/json');
							$fail["status"]=false;
							$fail["message"]="no data found operation";
							$this->response($this->json($fail),200);
							$this->dbClose();
							}

						
			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}
		}



		// send notication to users all 111 -> notify users that person is register now
		private function notify_user_login()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			if(!empty($variables)&&count($variables)>=10)
			{
							$result=mysqli_query($this->db,"CALL notify_user_login('".$variables["u_uid"]."')");
							if(mysqli_num_rows($result) > 0) // it reflect signle record;
							{
							$userDetails=array();
							while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
							{
							array_push($userDetails, $row["fcm_token"]);
							}
									$data = array(
									'title'=>$variables["title"],
									'message'=>$variables["message"],
									'color'=>$variables["color"],
									'summary'=>$variables["summary"],
									'big_text'=>$variables["big_text"],
									'nofitication_id'=>$variables["nofitication_id"],
									'big_allow_icon'=>$variables["big_allow_icon"],
									'url'=>"".$variables["url"],
									'type'=>$variables["type"]);

							
														// transfer data to FCM SERVER
														$target=$userDetails;
														$url = 'https://fcm.googleapis.com/fcm/send';
														//api_key available in Firebase Console -> Project Settings -> CLOUD 
														$server_key = 'AIzaSyBFf_si6NMSuUzu-OZZ2p6r_mCrKNrxnN4';

														$fields = array();
														if(is_array($target)){
														$fields['registration_ids'] = $target;
														}else{
														$fields['to'] = $target;
														}
														$fields['data'] = $data;

														//header with content_type api key
														$headers = array(
														'Content-Type:application/json',
														'Authorization:key='.$server_key
														);
														$ch = curl_init();
														curl_setopt($ch, CURLOPT_URL, $url);
														curl_setopt($ch, CURLOPT_POST, true);
														curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
														curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
														curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
														curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
														curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
														//echo json_encode($fields);
														$result = curl_exec($ch);
														curl_close($ch);
														if ($result === FALSE) 
														{
																header('Content-Type: application/json');
																$fail["status"]=false;
																$fail["message"]="no data found operation";
																$this->response($this->json($fail),200);
																$this->dbClose();
														}
														else
														{
															    header('Content-Type: application/json');
																$success["status"]=true;
																$success["message"]="response attached to data";
																$success["data"]=json_decode($result);
																$this->response($this->json($success),200);
																$this->dbClose();
														}

							}
							else
							{
							header('Content-Type: application/json');
							$fail["status"]=false;
							$fail["message"]="no data found operation";
							$this->response($this->json($fail),200);
							$this->dbClose();
							}

						
			}
			else
			{
			$this->dbClose();
			$this->response('',404);
			}

		}



		// add logs
		private function add_logs()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			$result=mysqli_query($this->db,"CALL insert_user_logs('".$variables["roll_number"]."','".$variables["type"]."','".$variables["u_uid"]."')");
			header('Content-Type: application/json');
			$success["status"]=true;
			$success["message"]="user log is added";
			$this->response($this->json($success),200);
			$this->dbClose();

		}


		// data to json
		private function json($data){
			if(is_array($data))
			{
				return json_encode($data);
			}
		}






}
$api = new USERS_API;
$api->processApi();



?>