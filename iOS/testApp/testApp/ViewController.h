//
//  ViewController.h
//  testApp
//
//  Created by Lucy Hutcheson on 12/10/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface ViewController : UIViewController
{
    NSMutableArray *firstName;
    NSMutableArray *lastName;
    NSMutableArray *email;
    NSMutableArray *phone;
    NSMutableArray *age;

}
@property(nonatomic, strong) IBOutlet UIButton *addButton;
@property(nonatomic, strong) IBOutlet UIButton *viewButton;

-(IBAction)onClick:(id)sender;
@end
